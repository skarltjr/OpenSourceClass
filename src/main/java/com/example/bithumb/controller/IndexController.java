package com.example.bithumb.controller;

import com.example.bithumb.dto.Data;
import com.example.bithumb.dto.Invest;
import com.example.bithumb.dto.OrderDto;
import com.example.bithumb.service.BitService;
import com.example.bithumb.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final BitService bitService;

    @PostMapping("/add/invest")
    public ResponseEntity firstInvest(@RequestBody Invest invest) {
        Wallet wallet = bitService.initialInvest(invest);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{id}")
    public ResponseEntity getWallet(@PathVariable Long id) {
        Wallet wallet = bitService.getMyWallet(id);
        if (wallet != null) {
            return ResponseEntity.ok(wallet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/{money}")
    public ResponseEntity addMoney(@PathVariable Long id, @PathVariable int money) {
        Wallet wallet = bitService.addMoney(id, money);
        if (wallet != null) {
            return ResponseEntity.ok(wallet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity removeWallet(@PathVariable Long id) {
        Long returnId = bitService.removeWallet(id);
        if (returnId == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(returnId);
        }
    }

    @GetMapping("/{kinds}/{current}")
    public ResponseEntity currentValue(@PathVariable String kinds, @PathVariable String current) {
        OrderDto result = bitService.getCurrent(kinds, current);
        Data data = bitService.computeTerm(result);
        return ResponseEntity.ok(data);
    }
}
