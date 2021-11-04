package com.example.bithumb.controller;


import com.example.bithumb.dto.WalletCreated;
import com.example.bithumb.dto.WalletForm;
import com.example.bithumb.dto.WalletResponse;
import com.example.bithumb.service.BitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private final BitService bitService;

    // 나중에 rest api -> mvc로 변경할 것
    @PostMapping("/wallet")
    public ResponseEntity createWallet(@RequestBody WalletForm form) {
        WalletCreated result = bitService.createWallet(form);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/wallet/{id}")
    public ResponseEntity getWallet(@PathVariable Long id) {
        WalletResponse myWallet = bitService.getMyWallet(id);
        return ResponseEntity.ok(myWallet);
    }

    @PostMapping("/invest/{id}")
    public ResponseEntity invest(@PathVariable Long id, @RequestParam long invest) {
        WalletResponse res = bitService.invest(id, invest);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity deleteWallet(@PathVariable Long id) {
        Long result = bitService.removeWallet(id);
        return ResponseEntity.ok(result);
    }

}
