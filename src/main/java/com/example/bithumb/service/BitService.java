package com.example.bithumb.service;

import com.example.bithumb.dto.*;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BitService {

    private final WalletRepository walletRepository;
    private final RestTemplate restTemplate;
    private static final String bithumbURI = "https://api.bithumb.com/public/ticker/";


    public void createWallet(WalletForm form) {
        String max_price = getCurrent(form.getCoin(),"KRW").getData().getMax_price();
        long price = Long.parseLong(max_price);
        Wallet wallet = Wallet.builder()
                .coin(form.getCoin())
                .base(form.getBase())
                .walletName(form.getWalletName())
                .current(form.getBase())
                .lastPrice(price)
                .build();
       walletRepository.save(wallet);
    }

    public WalletResponse getMyWallet(Long id) {
        if (walletRepository.existsById(id)) {
            Wallet wallet = walletRepository.findById(id).get();
            long now = computeCurrentValue(wallet);
            wallet.setCurrent(now);

            WalletResponse res = new WalletResponse();
            res.setBase(wallet.getBase());
            res.setWalletName(wallet.getWalletName());
            res.setCoin(wallet.getCoin());
            res.setCurrentPrice(wallet.getCurrent());
            return res;
        } else {
            return null;
        }
    }

    public WalletResponse invest(Long id, InvestForm form) {
        long invest = form.getInvest();
        if (!walletRepository.existsById(id)) {
            return null;
        } else {
            Wallet wallet = walletRepository.findById(id).get();
            wallet.setBase(wallet.getBase() + invest);
            wallet.setCurrent(wallet.getCurrent() + invest);
            String changed = getCurrent(wallet.getCoin(), "KRW").getData().getMax_price();
            wallet.setLastPrice(Long.parseLong(changed));
            Wallet save = walletRepository.save(wallet);

            WalletResponse res = new WalletResponse();
            res.setBase(wallet.getBase());
            res.setWalletName(wallet.getWalletName());
            res.setCoin(wallet.getCoin());
            res.setCurrentPrice(wallet.getCurrent());
            return res;
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

    private long computeCurrentValue(Wallet wallet) {
        String getCurrentPrice = getCurrent(wallet.getCoin(),"KRW").getData().getMax_price();
        int current = Integer.parseInt(getCurrentPrice);

        /** 가장 주요 로직 변경 폭을 구해야한다
         * 원래 변경 폭이 10.123%라면
         * 10.12 소숫점아래 2자리까지만 반영하기
         * */
        double originPercentage = ((double)current / wallet.getLastPrice());
        DecimalFormat form = new DecimalFormat("#.##");
        String format = form.format(originPercentage);
        float percentage = Float.parseFloat(format);

        long now = (long) (wallet.getBase() * percentage);
        return now;
    }

    public List<Result> getAll() {
        String current = "KRW";
        String[] str = {"BTC", "ETH", "XRP", "DOGE", "ADA"};
        List<OrderDto> list = new ArrayList<>();
        List<Result> result = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            OrderDto coin = getCurrent(str[i], current);
            list.add(coin);
            Data data = computeTerm(coin);
            Result res = new Result();
            res.setCoin(str[i]);
            res.setMax_price(data.getMax_price());

            DecimalFormat form = new DecimalFormat("#.##");
            double traded = Double.parseDouble(data.getUnits_traded_24H());
            String format = form.format(traded);
            res.setUnits_traded_24H(format);
            res.setFluctate_rate_24H(data.getFluctate_rate_24H());
            result.add(res);
        }

        //todo 결과에 코인 정보는 있는데 코인 이름이 없음 dto새로만들기
        return result;
    }

    public List<Wallet> getMyWalletAll() {
        List<Wallet> myWallets = walletRepository.findAll();
        return myWallets;
    }
}
