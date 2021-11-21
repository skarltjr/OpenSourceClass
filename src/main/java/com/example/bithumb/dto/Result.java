package com.example.bithumb.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Result {
    private String coin;
    private String max_price;
    private String units_traded_24H;
    private String fluctate_rate_24H;
}
