package com.example.bithumb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletResponse {
    private Long id;
    private String walletName;
    private String coin;
    private long base;
    private long currentPrice;
}
