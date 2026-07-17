package com.example.sbsample.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

// Searchボタン押下時に返却するレスポンスDTOのサンプル
@Data
@Builder
public class UserSearchListResponseDto {
    private int totalCount;
    private String message;
    private List<UserDetail> userList;

    @Data
    @Builder
    public static class UserDetail {
        private String userid;
        private String username;
        private String departmentname;
        private String sectionname;
        private String phone;
        private String address;
    }
}
