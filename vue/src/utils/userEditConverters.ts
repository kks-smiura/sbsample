/**
 * ユーザー編集フォームとAPI間のデータ変換ユーティリティ
 */
import type { UserEditType, UserEditRequestDto, UserEditResponseDto } from '../types/userEdit';
import type { UserEditFormValues } from '../validations/userEditRules';

/**
 * UserEditTypeからUserEditFormValuesに変換する関数
 * フォーム初期化時に使用
 */
export const createInitialValues = (source: UserEditType): UserEditFormValues => ({
  userid: source.userid,
  username: source.username,
  departmentname: source.departmentname,
  sectionname: source.sectionname,
  phone: source.phone ?? '', // NULLの場合は空文字に変換
  address: source.address,
});

/**
 * UserEditFormValuesからUserEditRequestDtoに変換する関数
 * API送信前に使用
 */
export const createUserEditRequestDto = (formValues: UserEditFormValues): UserEditRequestDto => ({
  userid: formValues.userid,
  username: formValues.username,
  departmentname: formValues.departmentname,
  sectionname: formValues.sectionname,
  phone: formValues.phone,
  address: formValues.address,
});
