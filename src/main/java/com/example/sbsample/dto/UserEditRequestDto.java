package com.example.sbsample.dto;

import lombok.Data;

@Data // Getter, Setterなどを自動生成 (Lombok)
public class UserEditRequestDto {
    private String userid;
    private String username;
    private String departmentname;
    private String sectionname;
    private String phone;
    private String address;
}
