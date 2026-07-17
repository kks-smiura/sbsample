<script setup lang="ts">
import axios from "axios";
import type { AxiosError, AxiosResponse } from "axios";
import { ref } from 'vue';
import { useField, useForm } from 'vee-validate';
import { userEditValidationSchema, type UserEditFormValues } from '../validations/userEditRules.ts';
import type { UserEditType, UserEditResponseDto } from '../types/userEdit';
import { defaultUser } from '../types/userEdit';
import { createInitialValues, createUserEditRequestDto } from '../utils/userEditConverters';

// インフォメーションメッセージ
const infoMessage = ref<string>('');
const errorMessages = ref<string[]>([]);

// Propsの定義（TypeScriptの型引数形式）
const props = defineProps<{
  user?: UserEditType
}>();

// フォームの各フィールドを1つのリアクティブなオブジェクトとして定義
const initialData = props.user || defaultUser;
const { errors, validate, resetForm, values } = useForm<UserEditFormValues>({
  validationSchema: userEditValidationSchema,
  initialValues: createInitialValues(initialData),
});

// 各フィールドの値を個別に取得
const { value: userid } = useField<string>('userid');
const { value: username } = useField<string>('username');
const { value: departmentname } = useField<string | undefined>('departmentname');
const { value: sectionname } = useField<string | undefined>('sectionname');
const { value: phone } = useField<string | undefined>('phone');
const { value: address } = useField<string | undefined>('address');

// バリデーションを実行し、エラーメッセージを更新する関数
const validateForm = async (): Promise<boolean> => {
  const result = await validate();
  errorMessages.value = Object.values(result.errors).filter((message): message is string => Boolean(message));
  return result.valid;
};

// 変更を保存ボタン 正常終了イベント
const onEntrySuccess = (response: AxiosResponse<UserEditResponseDto>) => {
    console.log('【外部成功処理】データ保存に成功しました！', response.data);
    
    const data = response.data;
    resetForm({
      values: createInitialValues(data),
    });

    console.log('updated form state', values);

    // メッセージエリアに成功メッセージを設定
    errorMessages.value = [];
    infoMessage.value = response.data.resultinformation;

    console.log('onEntrySuccessが終了しました。');
};

// 変更を保存ボタン エラーイベント
const onEntryError = (error: AxiosError) => {
    console.error('【外部エラー処理】エラーが発生しました', error)
    infoMessage.value = '';
    
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

// どのアクションで送信されたかを管理する一時的な状態
const submitType = ref('')

// Submitボタンのクリックイベントハンドラ
const handleSubmit = async () => {
  // 変更を保存ボタン押下時
  if (submitType.value === 'entry') {
      await entryClick()
  }
  // 他のSubmitボタンが追加された場合は、ここに条件分岐を追加する
}

// 変更を保存ボタン押下時の処理
const entryClick = async () => {
  // バリデーション
  if (!(await validateForm())) {
    return;
  }

  infoMessage.value = '';
  errorMessages.value = [];

  // 送信データの作成
  const userEditdto = createUserEditRequestDto(values);

  // POST呼び出し（async/await + try/catch で安定化）
  try {
    const response = await axios.post<UserEditResponseDto>('/useredit/entry', userEditdto);
    onEntrySuccess(response);
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
    </div>

    <!-- 編集フォーム -->
    <form class="space-y-5" @submit.prevent="handleSubmit">
      
      <!-- ユーザーID (読み取り専用) -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">ユーザーID</label>
        <input
          type="text"
          :value="userid"
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
          v-model="username"
          type="text"
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
            部名
          </label>
          <input
            v-model="departmentname"
            type="text"
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
            v-model="sectionname"
            type="text"
            placeholder="例: フロントエンド開発課"
            class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          />
        </div>
      </div>

      <!-- 電話番号 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">
          電話番号 <span class="text-red-500">*</span>
        </label>
        <input
          v-model="phone"
          type="tel"
          placeholder="例: 03-1234-5678"
          class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          :class="errors.phone ? 'border-red-400 focus:ring-red-300' : 'border-blue-200'"
        />
        <p v-if="errors.phone" class="text-xs text-red-500 mt-1">{{ errors.phone }}</p>
      </div>

      <!-- 住所 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">住所</label>
        <input
          v-model="address"
          type="text"
          placeholder="例: 東京都千代田区..."
          class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
        />
      </div>

      <!-- アクションボタン -->
      <div class="flex justify-end space-x-3 pt-4 border-t border-blue-100">
        <button
          type="submit"
          @click="submitType = 'entry'"
          class="px-5 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          変更を保存
        </button>
      </div>

    </form>
  </div>
</template>