<script setup lang="ts">
import { ref, reactive, watch } from 'vue'

// ユーザー情報の型定義
interface User {
  id: string
  name: string
  department: string
  section?: string
  phone?: string
  address?: string
}

// エラー状態の型定義
interface FormErrors {
  name?: string
  department?: string
}

// Propsの定義（TypeScriptの型引数形式）
// withDefaultsを使うことで、型安全にデフォルト値を設定できます
const props = withDefaults(defineProps<{
  user?: User
}>(), {
  user: () => ({
    id: 'USR-2026-001',
    name: '山田 太郎',
    department: '開発本部',
    section: 'フロントエンド開発課',
    phone: '03-1234-5678',
    address: '東京都千代田区大手町1-1-1'
  })
})

// Emitsの定義（型安全なイベント定義）
const emit = defineEmits<{
  (e: 'save', payload: User): void
  (e: 'cancel'): void
}>()

// フォームのリアクティブデータ（Propsの変更を監視して同期）
const form = reactive<User>({ ...props.user })

watch(
  () => props.user,
  (newUser) => {
    Object.assign(form, newUser)
  },
  { deep: true }
)

// バリデーション状態
const errors = ref<FormErrors>({})

// バリデーションロジック
const validateForm = (): boolean => {
  errors.value = {}
  let isValid = true

  if (!form.name.trim()) {
    errors.value.name = 'ユーザー名は必須項目です'
    isValid = false
  }
  if (!form.department.trim()) {
    errors.value.department = '部門名は必須項目です'
    isValid = false
  }
  
  return isValid
}

// 送信ハンドラー
const handleSubmit = (): void => {
  if (!validateForm()) return
  emit('save', { ...form })
}
</script>

<template>
  <div class="max-w-2xl mx-auto my-8 bg-blue-50/50 p-8 rounded-2xl border border-blue-100 shadow-sm">
    <!-- ヘッダーエリア -->
    <div class="border-b border-blue-100 pb-4 mb-6">
      <h2 class="text-xl font-bold text-blue-900">ユーザー情報編集</h2>
      <p class="text-xs text-blue-600/80 mt-1">
        ※ メンテナンス画面：システム管理権限で編集中のダミーテキストを表示しています。
      </p>
    </div>

    <!-- 編集フォーム -->
    <form @submit.prevent="handleSubmit" class="space-y-5">
      
      <!-- ユーザーID (読み取り専用) -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">ユーザーID</label>
        <input
          type="text"
          :value="form.id"
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
          type="text"
          v-model="form.name"
          placeholder="例: 山田 太郎"
          class="w-full px-4 py-2 bg-white border rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          :class="errors.name ? 'border-red-400 focus:ring-red-300' : 'border-blue-200'"
        />
        <p v-if="errors.name" class="text-xs text-red-500 mt-1">{{ errors.name }}</p>
      </div>

      <!-- 所属（部門・部署）の並び -->
      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <!-- 部門名 -->
        <div>
          <label class="block text-sm font-medium text-blue-800 mb-1">
            部門名 <span class="text-red-500">*</span>
          </label>
          <input
            type="text"
            v-model="form.department"
            placeholder="例: 開発本部"
            class="w-full px-4 py-2 bg-white border rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
            :class="errors.department ? 'border-red-400 focus:ring-red-300' : 'border-blue-200'"
          />
          <p v-if="errors.department" class="text-xs text-red-500 mt-1">{{ errors.department }}</p>
        </div>

        <!-- 部署名 -->
        <div>
          <label class="block text-sm font-medium text-blue-800 mb-1">部署名</label>
          <input
            type="text"
            v-model="form.section"
            placeholder="例: フロントエンド開発課"
            class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
          />
        </div>
      </div>

      <!-- 電話番号 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">電話番号</label>
        <input
          type="tel"
          v-model="form.phone"
          placeholder="例: 03-1234-5678"
          class="w-full px-4 py-2 bg-white border border-blue-200 rounded-lg text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-400 focus:border-blue-400"
        />
      </div>

      <!-- 住所 -->
      <div>
        <label class="block text-sm font-medium text-blue-800 mb-1">住所</label>
        <input
          type="text"
          v-model="form.address"
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
          type="submit"
          class="px-5 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          変更を保存
        </button>
      </div>

    </form>
  </div>
</template>