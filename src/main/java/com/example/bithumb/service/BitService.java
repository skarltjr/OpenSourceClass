package com.example.bithumb.service;

import com.example.bithumb.dto.Data;
import com.example.bithumb.dto.Invest;
import com.example.bithumb.dto.OrderDto;
import com.example.bithumb.repository.WalletRepository;
import com.example.bithumb.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class BitService {

    private final WalletRepository walletRepository;
    private final RestTemplate restTemplate;
    private static final String bithumbURI = "https://api.bithumb.com/public/ticker/";


    public Wallet initialInvest(Invest invest) {
        int base = invest.getBase();
        OrderDto coin = getCoin(invest.getCoin());
        int price = Integer.parseInt(coin.getData().getMax_price());

        Wallet wallet = Wallet.builder()
                .coin(invest.getCoin())
                .base(base)
                .current(base)
                .lastPrice(price)
                .build();

        return walletRepository.save(wallet);
    }


    public Wallet getMyWallet(Long id) {
        if (walletRepository.existsById(id)) {
            Wallet wallet = walletRepository.findById(id).get();
            int now = computeCurrentValue(wallet);
            wallet.setCurrent(now);
            return wallet;
        } else {
            return null;
        }
    }

    public Wallet addMoney(Long id, int money) {
        if (!walletRepository.existsById(id)) {
            return null;
        } else {
            Wallet wallet = walletRepository.findById(id).get();
            wallet.setBase(wallet.getBase() + money);
            wallet.setCurrent(wallet.getCurrent() + money);
            return wallet;
        }
    }

    public Long removeWallet(Long id) {
        if (!walletRepository.existsById(id)) {
            return null;
        } else {
            walletRepository.deleteById(id);
            return id;
        }
    }


    public OrderDto getCurrent(String bitKinds, String current) {
        String kinds = bitKinds;
        String selectedCurrent = current;

        String targetUri = bithumbURI + kinds + "_" + current;
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(targetUri);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(targetUri, HttpMethod.GET, httpEntity, OrderDto.class).getBody();
    }

    public Data computeTerm(OrderDto result) {
        return result.getData();
    }

    private OrderDto getCoin(String coin) {
        String kinds = coin;
        String current = "KRW";
        String targetUri = bithumbURI + kinds + "_" + current;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(targetUri, HttpMethod.GET, httpEntity, OrderDto.class).getBody();
    }

    private int computeCurrentValue(Wallet wallet) {
        String getCurrentPrice = getCoin(wallet.getCoin()).getData().getMax_price();
        int current = Integer.parseInt(getCurrentPrice);
        double percentage = current / wallet.getLastPrice();

        int now = (int) (wallet.getCurrent() * percentage);
        return now;
    }
}
