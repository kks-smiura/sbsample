package com.example.sbsample.dto;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Getter, Setterなどを自動生成 (Lombok)
public class UserMasterFormDTO {
    
    //@NotBlank(message = "名前は必須項目です")
    private String name;
    
    //@NotBlank(message = "メールアドレスは必須項目です")
    //@Email(message = "正しいメールアドレス形式で入力してください")
    private String email;
    
    //@NotBlank(message = "権限は必須項目です")
    private String role;
}