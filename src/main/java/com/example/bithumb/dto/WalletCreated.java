package com.example.bithumb.dto;

import com.example.bithumb.wallet.Wallet;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletCreated {
    private Long id;
    private String walletName;
    private String coin;
    private long base;

    public WalletCreated(Wallet wallet) {
        this.id = wallet.getId();
        this.walletName = wallet.getWalletName();
        this.coin = wallet.getCoin();
        this.base = wallet.getBase();
    }
}
