package com.example.bithumb.controller;

import com.example.bithumb.dto.Data;
import com.example.bithumb.service.BitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final BitService bitService;

    // 현재 전체 코인 가격 확인하기
    @GetMapping("/allCoins")
    public ResponseEntity getAll() {
        List<Data> list = bitService.getAll();
        return ResponseEntity.ok(list);
    }
}
