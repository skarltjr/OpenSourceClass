package com.example.bithumb.controller;

import com.example.bithumb.dto.WalletCreated;
import com.example.bithumb.dto.WalletForm;
import com.example.bithumb.repository.WalletRepository;
import com.example.bithumb.service.BitService;
import com.example.bithumb.wallet.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BitService service;
    @Autowired
    WalletRepository repository;
    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("지갑 생성")
    void create() throws Exception {
        WalletForm setUp = form();
        mockMvc.perform(post("/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(setUp)))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("walletName").value("kiseok"))
                .andExpect(jsonPath("coin").value("BTC"))
                .andExpect(jsonPath("base").value("1000000"))
                .andDo(print());
    }

    @Test
    @DisplayName("지갑 조회")
    void getWallet() throws Exception {
        WalletForm form = form();
        WalletCreated wallet = service.createWallet(form);
        mockMvc.perform(get("/wallet/{id}", wallet.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("walletName").value("kiseok"))
                .andExpect(jsonPath("coin").value("BTC"))
                .andExpect(jsonPath("base").value("1000000"))
                .andExpect(jsonPath("currentPrice").value("1000000"))
                .andDo(print());
    }

    @Test
    @DisplayName("지갑 삭제")
    void deleteWallet() throws Exception {
        WalletForm form = form();
        WalletCreated wallet = service.createWallet(form);

        String result = mockMvc.perform(post("/delete/{id}", wallet.getId()))
                .andReturn().getResponse().getContentAsString();
        assertThat(result).isEqualTo(wallet.getId().toString());
    }

    @Test
    @DisplayName("추가 투자")
    void invest() throws Exception {
        WalletForm form = form();
        WalletCreated wallet = service.createWallet(form);

        mockMvc.perform(post("/invest/{id}",wallet.getId())
            .param("invest","30000"))
                .andExpect(jsonPath("walletName").value("kiseok"))
                .andExpect(jsonPath("coin").value("BTC"))
                .andExpect(jsonPath("base").value("1030000"))
                .andExpect(jsonPath("currentPrice").value("1030000"))
                .andDo(print());
    }

    @Test
    @DisplayName("가격 변동 반영")
    void changedPrice() throws Exception {
        WalletForm form = form();
        WalletCreated wallet = service.createWallet(form);
        Wallet wallet1 = repository.findById(wallet.getId()).get();
        wallet1.setLastPrice(50000000);
        Wallet saved = repository.save(wallet1);

        /** 가장 주요 로직 변경 폭을 구해야한다
         * 원래 변경 폭이 10.123%라면
         * 10.12% 소숫점아래 2자리까지만 반영하기
         * */
        long currentPrice = Long.parseLong(service.getCurrent("BTC", "KRW").getData().getMax_price());
        double originPercentage = ((double)currentPrice / 50000000);

        DecimalFormat decimal = new DecimalFormat("#.##");
        String format = decimal.format(originPercentage);
        float percentage = Float.parseFloat(format);

        String result = ((long) (saved.getBase() * percentage)) + "";

        mockMvc.perform(get("/wallet/{id}", saved.getId()))
                .andExpect(jsonPath("currentPrice").value(result))
                .andDo(print());
    }
    private WalletForm form() {
        WalletForm walletForm = new WalletForm();
        walletForm.setWalletName("kiseok");
        walletForm.setBase(1000000);
        walletForm.setCoin("BTC");
        return walletForm;
    }
}