package com.example.sbsample.service;

import org.springframework.stereotype.Service;
import com.example.sbsample.dto.UserEditRequestDto;
import com.example.sbsample.dto.UserEditResponseDto;

@Service
public class UserEditService {

    public String editUser() {
        // ここにユーザー編集のロジックを実装します
        return "User edited successfully!";
    }

    /**
     * ユーザー登録処理（サンプルのためDB保存は省略）
     */
    public UserEditResponseDto registUser(UserEditRequestDto request) {
        // 本来はここでDBの登録処理等を記述し、結果をResponseDtoで返す
        String resultinformation = request.getUsername() + "様の登録が完了しました。";
        UserEditResponseDto  retDto = UserEditResponseDto.builder()
            .userid("USR01-000001")
            .username("豊臣 秀吉")
            .departmentname("第一サービス部")
            .sectionname("営業課")
            .phone("001-0001-0001")
            .address("福岡県福岡市東区箱崎７丁目８−２")
            .resultinformation(resultinformation)
            .build();
        return retDto;
    }
}