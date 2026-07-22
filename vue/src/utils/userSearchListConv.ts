/**
 * ユーザー情報検索フォームとAPI間のデータ変換ユーティリティ
 */
import type { SearchConditions } from '../types/userSearchListTypes.ts';

export const defaultConditions: SearchConditions = {
  userid: '',
  username: '',
  departmentname: '',
  sectionname: '',
  phone: '',
  address: ''
}
