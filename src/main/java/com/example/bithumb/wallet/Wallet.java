package com.example.bithumb.wallet;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;
    private String walletName;
    private String coin;
    private long base; // 투자금
    private long current; // 현재 금액
    private long lastPrice; // 마지막 추가 투자금 투입 때 당시 해당코인 금액
}
