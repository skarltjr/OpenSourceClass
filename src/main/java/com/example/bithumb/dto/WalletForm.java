package com.example.bithumb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletForm {
    private String walletName;
    private String coin;
    private long base;
}
