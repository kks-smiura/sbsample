<script setup lang="ts">
import axios from "axios";
import type { AxiosError, AxiosResponse } from "axios";
import { ref } from 'vue';

// インフォメーションメッセージ
const infoMessage = ref<string>('');
const errorMessages = ref<string[]>([]);

// ユーザー情報の型定義
interface UserEditType {
  userid: string
  username: string
  departmentname: string
  sectionname?: string
  phone?: string
  address?: string
}

// エラー状態の型定義
interface FormErrors {
  username?: string
  departmentname?: string
}

// Propsの定義（TypeScriptの型引数形式）n
// withDefaultsを使わない形式に変換
const props = defineProps<{
  user?: UserEditType
}>();

// props.user が提供されない場合のデフォルト値
const defaultUser: UserEditType = {
  userid: 'USR-2026-001',
  username: '山田 太郎',
  departmentname: '開発本部',
  sectionname: 'フロントエンド開発課',
  phone: '03-1234-5678',
  address: '東京都千代田区大手町1-1-1'
};

// RequestDTOの定義
interface UserEditRequestDto {
    userid: string;
    username: string;
    departmentname?: string;  // 任意項目
    sectionname?: string;     // 任意項目
    phone?: string;       // 任意項目
    address?: string;     // 任意項目
};

// ResponseDTOの定義
interface UserEditResponseDto {
    userid: string;
    username: string;
    departmentname: string;
    sectionname: string;
    phone: string;
    address: string;
    resultinformation: string
};

const responseDto = ref<UserEditResponseDto | null>(null);

// RequestDTOの定義
const CreateUserEditRequestDto = (overrides: Partial<UserEditRequestDto> = {}): UserEditRequestDto => ({
    userid: editForm.value.userid,
    username: editForm.value.username,
    departmentname : editForm.value.departmentname,  // 任意項目
    sectionname : editForm.value.sectionname,     // 任意項目
    phone: editForm.value.phone,
    address: editForm.value.address
});

// Emitsの定義（型安全なイベント定義）
const emit = defineEmits<{
  (e: 'save', payload: UserEditType): void
  (e: 'cancel'): void
}>();

// フォームの各フィールドを1つのリアクティブなオブジェクトとして定義
const initialData = props.user || defaultUser;
const editForm = ref<UserEditType>({
  userid: initialData.userid,
  username: initialData.username,
  departmentname: initialData.departmentname,
  sectionname: initialData.sectionname,
  phone: initialData.phone,
  address: initialData.address,
});

// バリデーション状態
const errors = ref<FormErrors>({});

// バリデーションロジック
const validateForm = (): boolean => {
  errors.value = {}
  errorMessages.value = []
  let isValid = true

  if (!editForm.value.username.trim()) {
    errors.value.username = 'ユーザー名は必須項目です'
    errorMessages.value.push(errors.value.username)
    isValid = false
  }
  if (!editForm.value.departmentname.trim()) {
    errors.value.departmentname = '部門名は必須項目です'
    errorMessages.value.push(errors.value.departmentname)
    isValid = false
  }
  
  return isValid
};

// 変更を保存ボタン 正常終了イベント
const onEntrySuccess = (response: AxiosResponse<UserEditResponseDto>) => {
    console.log('【外部成功処理】データ保存に成功しました！', response.data);
    
    // 親コンポーネントに変更を通知
    // フォームの更新より先にemitを実行することで、親コンポーネントの処理による
    // 意図しない上書きを防ぎます。
    //emit('save', response.data);
    
    /*
    form.username = response.data.username;
    form.departmentname = response.data.departmentname;
    form.sectionname = response.data.sectionname;
    form.phone = response.data.phone;
    form.address = response.data.address;
    */

    // レスポンスデータ(response.data)のキーをループ処理します。
    // これにより、レスポンスに含まれるプロパティのみが form オブジェクトに安全に代入され、
    // 意図せず値が undefined で上書きされることを防ぎます。
    const data = response.data;
    editForm.value = {
      userid: data.userid,
      username: data.username,
      departmentname: data.departmentname,
      sectionname: data.sectionname,
      phone: data.phone,
      address: data.address,
    };
   /*
    editForm.value = {
      userid: 'TEST-2026-001',  // テスト用に固定値を設定
      username: 'TEST-豊臣秀吉',  // テスト用に固定値を設定
      departmentname: 'TEST-営業本部',  // テスト用に固定値を設定
      sectionname: 'TEST-営業一課',  // テスト用に固定値を設定
      phone: 'TEST-03-1234-5678',  // テスト用に固定値を設定
      address: 'TEST-東京都千代田区大手町1-1-1',  // テスト用に固定値を設定
    };
    */
    responseDto.value = data;
    console.log('updated form state', editForm.value);

    // メッセージエリアに成功メッセージを設定
    errorMessages.value = [];
    infoMessage.value = response.data.resultinformation;

    console.log('onEntrySuccessが終了しました。');
};

// 変更を保存ボタン エラーイベント
const onEntryError = (error: AxiosError) => {
    console.error('【外部エラー処理】エラーが発生しました', error)
    infoMessage.value = '';
  responseDto.value = null;
    
    // エラーメッセージを判定してリアクティブな変数に格納
    if (error.response) {
      const responseData = error.response.data as { message?: string; resultinformation?: string } | string | undefined;
      if (typeof responseData === 'string') {
        errorMessages.value = [responseData];
      } else {
        errorMessages.value = [responseData?.message || responseData?.resultinformation || 'サーバーエラーが発生しました。'];
      }
    } else {
      errorMessages.value = ['ネットワークエラーが発生しました。'];
    }
};

// 変更を保存ボタン 送信処理
const sendEntrySubmit = async () => {
  // バリデーション
  if (!validateForm()) {
    return;
  }

  infoMessage.value = '';
  errorMessages.value = [];

  // 送信データの作成
  const userEditdto = CreateUserEditRequestDto();

  // POST呼び出し（async/await + try/catch で安定化）
  try {
    const response = await axios.post<UserEditResponseDto>('/useredit/entry', userEditdto);
    await onEntrySuccess(response as AxiosResponse<UserEditResponseDto>);
  } catch (err) {
    onEntryError(err as AxiosError);
  }
}
</script>

<template>
  <div class="max-w-2xl mx-auto my-8 bg-blue-50/50 p-8 rounded-2xl border border-blue-100 shadow-sm">
    <!-- インフォメーションメッセージエリア -->
    <div
      v-if="infoMessage"
      class="mb-6 p-4 bg-green-100 border border-green-200 text-green-800 text-sm rounded-lg"
      role="alert"
    >
      {{ infoMessage }}
    </div>
    <div
      v-if="errorMessages.length"
      class="mb-6 p-4 bg-red-100 border border-red-200 text-red-800 text-sm rounded-lg"
      role="alert"
    >
      <ul class="list-disc pl-5 space-y-1">
        <li v-for="message in errorMessages" :key="message">
          {{ message }}
        </li>
      </ul>
    </div>

    <!-- ヘッダーエリア -->
    <div class="border-b border-blue-100 pb-4 mb-6">
      <h2 class="text-xl font-bold text-blue-900">ユーザー情報編集</h2>
      <p class="text-xs text-blue-600/80 mt-1">
        ※ メンテナンス画面：システム管理権限で編集中のダミーテキストを表示しています。
      </p>
      <p class="text-xs text-blue-700 mt-2">状態: {{ editForm.username }} / {{ editForm.departmentname }}</p>
    </div>

    <!-- 編集フォーム -->
    <form class="space-y-5">
      
      <!-- ユーザーID (読み取り専用) -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">ユーザーID</label>
        <input
          type="text"
          :value="editForm.userid"
          disabled
          class="w-full px-4 py-2 bg-blue-100/50 border border-blue-200 text-blue-600 rounded-lg cursor-not-allowed text-sm font-mono"
        />
      </div>

      <!-- ユーザー名 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">
          ユーザー名 <span class="text-red-500">*</span>
        </label>
        <input
          :key="`username-${editForm.username}`"
          type="text"
          :value="editForm.username"
          @input="editForm.username = ($event.target as HTMLInputElement).value"
          placeholder="例: 山田 太郎"
          class="w-full px-4 py-2 bg-white border rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          :class="errors.username ? 'border-red-400 focus:ring-red-300' : 'border-blue-200'"
        />
        <p v-if="errors.username" class="text-xs text-red-500 mt-1">{{ errors.username }}</p>
      </div>

      <!-- 所属（部・課）の並び -->
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <!-- 部名 -->
        <div>
          <label class="block text-sm font-medium text-blue-800 mb-1">
            部名 <span class="text-red-500">*</span>
          </label>
          <input
            :key="`departmentname-${editForm.departmentname}`"
            type="text"
            :value="editForm.departmentname"
            @input="editForm.departmentname = ($event.target as HTMLInputElement).value"
            placeholder="例: 開発本部"
            class="w-full px-4 py-2 bg-white border rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
            :class="errors.departmentname ? 'border-red-400 focus:ring-red-300' : 'border-blue-200'"
          />
          <p v-if="errors.departmentname" class="text-xs text-red-500 mt-1">{{ errors.departmentname }}</p>
        </div>

        <!-- 課名 -->
        <div>
          <label class="block text-sm font-medium text-blue-800 mb-1">課名</label>
          <input
            :key="`sectionname-${editForm.sectionname || ''}`"
            type="text"
            :value="editForm.sectionname"
            @input="editForm.sectionname = ($event.target as HTMLInputElement).value"
            placeholder="例: フロントエンド開発課"
            class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          />
        </div>
      </div>

      <!-- 電話番号 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">電話番号</label>
        <input
          :key="`phone-${editForm.phone || ''}`"
          type="tel"
          :value="editForm.phone"
          @input="editForm.phone = ($event.target as HTMLInputElement).value"
          placeholder="例: 03-1234-5678"
          class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
        />
      </div>

      <!-- 住所 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">住所</label>
        <input
          :key="`address-${editForm.address || ''}`"
          type="text"
          :value="editForm.address"
          @input="editForm.address = ($event.target as HTMLInputElement).value"
          placeholder="例: 東京都千代田区..."
          class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
        />
      </div>

      <!-- アクションボタン -->
      <div class="flex justify-end space-x-3 pt-4 border-t border-blue-100">
        <button
          type="button"
          @click="emit('cancel')"
          class="px-5 py-2 text-sm font-medium text-blue-700 bg-white border border-blue-200 rounded-lg hover:bg-blue-50 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-300"
        >
          キャンセル
        </button>
        <button
          type="button"
          @click="sendEntrySubmit"
          class="px-5 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          変更を保存
        </button>
      </div>

    </form>
  </div>
</template>