package com.example.sbsample.dto;

import lombok.Builder;
import lombok.Data;

// UserEditFormから送られてくるリクエストデータ
@Data // Getter, Setterなどを自動生成 (Lombok)
@Builder
public class UserEditFormResponseDto {
    private String userid;
    private String username;
    private String departmentname;
    private String sectionname;
    private String phone;
    private String address;
    private String resultinformation;   
}
