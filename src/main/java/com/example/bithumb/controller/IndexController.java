package com.example.bithumb.controller;

import com.example.bithumb.dto.Data;

import com.example.bithumb.service.BitService;
import com.example.bithumb.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final BitService bitService;

    // 현재 전체 코인 가격 확인하기
    @GetMapping("/index")
    public String getAll(Model model) {
        List<Data> list = bitService.getAll();
        List<Wallet> myWalletAll = bitService.getMyWalletAll();
        model.addAttribute("coins", list);
        model.addAttribute("myWallets", myWalletAll);
        return "index";
    }
}
