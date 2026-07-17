package com.example.sbsample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sbsample.dto.UserEditRequestDto;
import com.example.sbsample.dto.UserEditResponseDto;
import com.example.sbsample.service.UserEditService;

@RestController
@RequestMapping("/useredit")
@CrossOrigin(origins = "http://localhost:5173") // Vue開発サーバー(Vite)からのアクセスを許可
public class UserEditFormController {

    private final UserEditService userEditService;

    public UserEditFormController(UserEditService userEditService) {
        this.userEditService = userEditService;
    }

    /**
     * ユーザー情報をPOSTで受け取って登録処理を行うエンドポイント
     * URL: POST http://localhost:8080/useredit/entry
     */
    @PostMapping("/entry")
    public ResponseEntity<UserEditResponseDto> createUser(@RequestBody UserEditRequestDto request) {
        
        // Serviceのビジネスロジックを呼び出す
        UserEditResponseDto response = userEditService.registUser(request);
        // HTTPステータス 200 (OK) と一緒に処理結果のJSONを返す
        return ResponseEntity.ok(response);
    }
}