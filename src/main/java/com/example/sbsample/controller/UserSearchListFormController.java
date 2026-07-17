package com.example.sbsample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sbsample.dto.UserSearchListRequestDto;
import com.example.sbsample.dto.UserSearchListResponseDto;
import com.example.sbsample.service.UserSearchListService;

@RestController
@RequestMapping("/usersearch")
@CrossOrigin(origins = "http://localhost:5173") // Vue開発サーバー(Vite)からのアクセスを許可
public class UserSearchListFormController {

    private final UserSearchListService userSearchListService;

    public UserSearchListFormController(UserSearchListService userSearchListFormService) {
        this.userSearchListService = userSearchListFormService;
    }

    /**
     * ユーザー情報をPOSTで受け取って登録処理を行うエンドポイント
     * URL: POST http://localhost:8080/usersearch/searchlist
     */
    @PostMapping("/searchlist")
    public ResponseEntity<UserSearchListResponseDto> searchList(@RequestBody UserSearchListRequestDto request) {
        // Serviceのビジネスロジックを呼び出す
        UserSearchListResponseDto response = userSearchListService.searchList(request);
        // HTTPステータス 200 (OK) と一緒に処理結果のJSONを返す
        return ResponseEntity.ok(response);
    }
}