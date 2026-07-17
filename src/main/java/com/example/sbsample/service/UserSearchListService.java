package com.example.sbsample.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.sbsample.dto.UserSearchListRequestDto;
import com.example.sbsample.dto.UserSearchListResponseDto;

@Service
public class UserSearchListService {

    /**
     * ユーザー検索処理（サンプルのためDB検索は省略）
     */
    public UserSearchListResponseDto searchList(UserSearchListRequestDto request) {
        //一覧表示内容をダミーで生成
        UserSearchListResponseDto.UserDetail userDetail01 = UserSearchListResponseDto.UserDetail.builder()
            .userid("USR01-000001")
            .username("織田 信長")
            .departmentname("製品開発部")
            .sectionname("研究課")
            .phone("001-0001-0001")
            .address("福岡県福岡市東区箱崎７丁目８−２")
            .build();
        UserSearchListResponseDto.UserDetail userDetail02 = UserSearchListResponseDto.UserDetail.builder()
            .userid("USR01-000002")
            .username("豊臣 秀吉")
            .departmentname("第一サービス部")
            .sectionname("営業課")
            .phone("001-0001-0001")
            .address("福岡県福岡市東区箱崎７丁目８−２")
            .build();
        UserSearchListResponseDto.UserDetail userDetail03 = UserSearchListResponseDto.UserDetail.builder()
            .userid("USR01-000003")
            .username("徳川 家康")
            .departmentname("総務部")
            .sectionname("総務課")
            .phone("001-0001-0001")
            .address("福岡県福岡市中央区天神１丁目３−３３")
            .build();
        List<UserSearchListResponseDto.UserDetail> userList = List.of(userDetail01, userDetail02, userDetail03);
        // DTOにセット
        UserSearchListResponseDto retDto = UserSearchListResponseDto.builder()
            .totalCount(3)        // 件数
            .userList(userList)   // 一覧表示結果
            .message("検索が完了しました。")
            .build();
        return retDto;
    }
}