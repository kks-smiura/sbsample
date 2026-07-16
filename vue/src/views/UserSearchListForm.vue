<script setup lang="ts">
import axios from "axios";
import { ref } from 'vue'

interface UserRow {
  userid: string
  username: string
  departmentname: string
  sectionname: string
  phone: string
  address: string
}

interface SearchConditions {
  userid: string
  username: string
  departmentname: string
  sectionname: string
  phone: string
  address: string
}

interface UserSearchListFormResponseDto {
  totalCount: number
  message: string
  userList: UserRow[]
}

const defaultConditions: SearchConditions = {
  userid: '',
  username: '',
  departmentname: '',
  sectionname: '',
  phone: '',
  address: ''
}

const conditions = ref<SearchConditions>({ ...defaultConditions })

const allUsers = ref<UserRow[]>([
  {
    userid: 'USR-2026-001',
    username: '山田 太郎',
    departmentname: '開発本部',
    sectionname: 'フロントエンド開発課',
    phone: '03-1234-5678',
    address: '東京都千代田区大手町1-1-1'
  },
  {
    userid: 'USR-2026-002',
    username: '佐藤 花子',
    departmentname: '開発本部',
    sectionname: 'バックエンド開発課',
    phone: '03-9876-5432',
    address: '東京都中央区日本橋2-2-2'
  },
  {
    userid: 'USR-2026-003',
    username: '鈴木 一郎',
    departmentname: '営業本部',
    sectionname: '営業一課',
    phone: '06-1111-2222',
    address: '大阪府大阪市北区梅田3-3-3'
  },
  {
    userid: 'USR-2026-004',
    username: '高橋 美咲',
    departmentname: '管理本部',
    sectionname: '総務課',
    phone: '052-333-4444',
    address: '愛知県名古屋市中区栄4-4-4'
  },
  {
    userid: 'USR-2026-005',
    username: '伊藤 健',
    departmentname: '品質保証本部',
    sectionname: '品質管理課',
    phone: '092-555-6666',
    address: '福岡県福岡市博多区博多駅前5-5-5'
  }
])

const displayedUsers = ref<UserRow[]>([...allUsers.value])

const normalize = (value: string): string => value.trim()

// Searchボタン押下
const doSearch = async (): Promise<void> => {

  try {
    console.log('doSearch called with conditions:', conditions.value)

    // searchlist APIにPOSTリクエストを送信
    const response = await axios.post<UserSearchListFormResponseDto>('http://localhost:8080/usersearch/searchlist', {
      userid: normalize(conditions.value.userid),
      username: normalize(conditions.value.username),
      departmentname: normalize(conditions.value.departmentname),
      sectionname: normalize(conditions.value.sectionname),
      phone: normalize(conditions.value.phone),
      address: normalize(conditions.value.address)
    })

    // レスポンスのステータスコードをチェック
    if (response.status != 200) {
      throw new Error(`Search request failed: ${response.status}`)
    }

    const data = response.data
    const rows = Array.isArray(data.userList) ? data.userList : []

    displayedUsers.value = rows.filter((row): row is UserRow => {
      return (
        row != null &&
        typeof row === 'object' &&
        'userid' in row &&
        'username' in row &&
        'departmentname' in row &&
        'sectionname' in row &&
        'phone' in row &&
        'address' in row
      )
    })

    console.log('doSearch end')

  } catch (error) {
    console.error('User search failed', error)
    displayedUsers.value = []
  }
}

const clearSearch = (): void => {
  conditions.value = { ...defaultConditions }
  displayedUsers.value = [...allUsers.value]
}
</script>

<template>
  <main class="search-list-page">
    <section class="search-card fade-in-up">
      <header class="search-header">
        <h1>User Search List</h1>
        <p>ユーザー情報の条件検索と一覧表示</p>
      </header>

      <div class="search-form-grid">
        <label>
          <span>UserID</span>
          <input v-model="conditions.userid" type="text" placeholder="例: USR-2026" />
        </label>

        <label>
          <span>UserName</span>
          <input v-model="conditions.username" type="text" placeholder="例: 山田" />
        </label>

        <label>
          <span>DepartmentName</span>
          <input v-model="conditions.departmentname" type="text" placeholder="例: 開発本部" />
        </label>

        <label>
          <span>SectionName</span>
          <input v-model="conditions.sectionname" type="text" placeholder="例: 開発課" />
        </label>

        <label>
          <span>Phone</span>
          <input v-model="conditions.phone" type="text" placeholder="例: 03-" />
        </label>

        <label>
          <span>Address</span>
          <input v-model="conditions.address" type="text" placeholder="例: 東京都" />
        </label>
      </div>

      <div class="button-row">
        <button type="button" class="button button-search" @click="doSearch">Search</button>
        <button type="button" class="button button-clear" @click="clearSearch">Clear</button>
      </div>
    </section>

    <section class="list-card fade-in-up delay-1">
      <div class="list-title-row">
        <h2>検索結果一覧</h2>
        <span>{{ displayedUsers.length }} 件</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>UserID</th>
              <th>UserName</th>
              <th>DepartmentName</th>
              <th>SectionName</th>
              <th>Phone</th>
              <th>Address</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in displayedUsers" :key="user.userid">
              <td>{{ user.userid }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.departmentname }}</td>
              <td>{{ user.sectionname }}</td>
              <td>{{ user.phone }}</td>
              <td>{{ user.address }}</td>
            </tr>
            <tr v-if="displayedUsers.length === 0">
              <td colspan="6" class="empty-row">該当データがありません</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </main>
</template>

<style scoped>
:global(body) {
  background:
    radial-gradient(circle at 12% 8%, #dbedff 0, transparent 34%),
    radial-gradient(circle at 86% 16%, #cce4ff 0, transparent 30%),
    linear-gradient(180deg, #f5fbff 0%, #eef7ff 100%);
}

.search-list-page {
  width: min(1220px, 95%);
  margin: 28px auto 36px;
  display: grid;
  gap: 20px;
  color: #0e2e4f;
  font-family: 'Segoe UI', 'Yu Gothic UI', 'Hiragino Kaku Gothic ProN', sans-serif;
}

.search-card,
.list-card {
  border: 1px solid #d9e8f7;
  border-radius: 20px;
  background: linear-gradient(160deg, rgb(255 255 255 / 92%), rgb(238 247 255 / 95%));
  box-shadow:
    0 10px 30px rgb(90 148 201 / 10%),
    inset 0 1px 0 rgb(255 255 255 / 70%);
  backdrop-filter: blur(6px);
}

.search-card {
  padding: 20px;
}

.list-card {
  padding: 16px 16px 18px;
}

.search-header h1 {
  margin: 0;
  font-size: clamp(1.4rem, 1.6vw, 1.8rem);
  font-weight: 700;
  letter-spacing: 0.02em;
}

.search-header p {
  margin: 4px 0 0;
  font-size: 0.92rem;
  color: #44729e;
}

.search-form-grid {
  margin-top: 16px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}

label {
  display: grid;
  gap: 6px;
}

label span {
  font-size: 0.8rem;
  font-weight: 600;
  color: #2f5e8d;
}

input {
  width: 100%;
  border: 1px solid #bfd8ef;
  border-radius: 10px;
  background: #fff;
  color: #153c62;
  padding: 10px 12px;
  font-size: 0.92rem;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

input:focus {
  outline: none;
  border-color: #5ba6e6;
  box-shadow: 0 0 0 3px rgb(110 173 233 / 20%);
}

.button-row {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.button {
  border: 0;
  border-radius: 999px;
  padding: 10px 20px;
  font-size: 0.9rem;
  font-weight: 700;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, opacity 0.2s ease;
}

.button:hover {
  transform: translateY(-1px);
}

.button-search {
  color: #fff;
  background: linear-gradient(120deg, #2f96e0, #3f81d8);
  box-shadow: 0 8px 18px rgb(58 131 205 / 26%);
}

.button-clear {
  color: #225682;
  background: #e7f1fb;
  box-shadow: 0 4px 12px rgb(132 172 212 / 25%);
}

.list-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.list-title-row h2 {
  margin: 0;
  font-size: 1.05rem;
  font-weight: 700;
  color: #224f7c;
}

.list-title-row span {
  border: 1px solid #bdd9f1;
  background: #eff7ff;
  color: #2f628f;
  border-radius: 999px;
  padding: 3px 10px;
  font-size: 0.8rem;
  font-weight: 700;
}

.table-wrap {
  overflow-x: auto;
  border: 1px solid #d4e5f5;
  border-radius: 14px;
  background: rgb(255 255 255 / 85%);
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 980px;
}

th,
td {
  text-align: left;
  padding: 12px 14px;
  border-bottom: 1px solid #e5eff8;
  font-size: 0.88rem;
}

th {
  position: sticky;
  top: 0;
  background: linear-gradient(180deg, #e8f4ff 0%, #def0ff 100%);
  color: #265884;
  font-weight: 700;
  letter-spacing: 0.02em;
}

tbody tr {
  transition: background-color 0.2s ease;
}

tbody tr:hover {
  background: #f2f8ff;
}

.empty-row {
  text-align: center;
  padding: 20px;
  color: #6084a9;
}

.fade-in-up {
  opacity: 0;
  transform: translateY(10px);
  animation: fade-in-up 0.5s ease forwards;
}

.delay-1 {
  animation-delay: 0.12s;
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (width <= 980px) {
  .search-form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (width <= 700px) {
  .search-list-page {
    width: min(100%, 96%);
    margin-top: 16px;
  }

  .search-card,
  .list-card {
    border-radius: 14px;
  }

  .search-form-grid {
    grid-template-columns: 1fr;
  }

  .button-row {
    justify-content: stretch;
  }

  .button {
    flex: 1;
  }
}
</style>