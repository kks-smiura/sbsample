/**
 * ユーザー情報検索フォームとAPI間のデータ型定義
 */
// ユーザー情報の型定義
export interface UserRow {
  userid: string
  username: string
  departmentname: string
  sectionname: string
  phone: string
  address: string
}

export interface SearchConditions {
  userid: string
  username: string
  departmentname: string
  sectionname: string
  phone: string
  address: string
}

export interface UserSearchListResponseDto {
  totalCount: number
  message: string
  userList: UserRow[]
}
