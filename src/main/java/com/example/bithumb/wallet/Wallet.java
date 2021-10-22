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

    private String coin;
    private int base; // 투자금
    private int current; // 현재 금액
    private int lastPrice; // 마지막 추가 투자금 투입 때 당시 해당코인 금액
}
