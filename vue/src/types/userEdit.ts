/**
 * ユーザー編集フォームとAPI間のデータ型定義ユーティリティ
 */
// ユーザー情報の型定義
export interface UserEditType {
  userid: string;
  username: string;
  departmentname: string;
  sectionname?: string;
  phone?: string;
  address?: string;
}

// リクエストDTO（サーバーへ送信）
export interface UserEditRequestDto {
  userid: string;
  username: string;
  departmentname?: string;
  sectionname?: string;
  phone?: string;
  address?: string;
}

// レスポンスDTO（サーバーから受信）
export interface UserEditResponseDto {
  userid: string;
  username: string;
  departmentname: string;
  sectionname: string;
  phone: string;
  address: string;
  resultinformation: string;
}

// デフォルトユーザー情報
export const defaultUser: UserEditType = {
  userid: 'USR-2026-001',
  username: '山田 太郎',
  departmentname: '開発本部',
  sectionname: 'フロントエンド開発課',
  phone: '03-1234-5678',
  address: '東京都千代田区大手町1-1-1'
};
