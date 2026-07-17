/**
 * ユーザー編集フォームとAPI間のデータ変換ユーティリティ
 */
import * as yup from 'yup';

// ユーザー編集フォーム用の入力チェックを Yup で定義します。
export const userEditValidationSchema = yup.object({
  userid: yup
    .string()
    .trim()
    .required('ユーザーIDは必須項目です。'),
  username: yup
    .string()
    .trim()
    .required('ユーザー名は必須項目です。'),
  departmentname: yup
    .string()
    .trim()
    .optional(),
  sectionname: yup
    .string()
    .trim()
    .optional(),
  phone: yup
    .string()
    .trim()
    .matches(/^[0-9-]+$/, '電話番号は数字とハイフン(-)のみ入力できます。')
    .max(15, '電話番号は15桁以内で入力してください。')
    .required('電話番号は必須項目です。'),
  address: yup
    .string()
    .trim()
    .optional(),
});

// 既存参照互換のための別名。
export const userValidationSchema = userEditValidationSchema;

// TypeScript用の型定義をスキーマから自動抽出
export type UserEditFormValues = yup.InferType<typeof userEditValidationSchema>;
