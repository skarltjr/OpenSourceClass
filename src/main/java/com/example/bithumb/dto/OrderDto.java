package com.example.bithumb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private String status;
    private Data data;
}
