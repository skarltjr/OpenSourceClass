package com.example.bithumb;

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

    private final RestTemplate restTemplate;

    private String bithumbURI = "https://api.bithumb.com/public/ticker/";

    public OrderDto getCurrent(String bitKinds,String current) {
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
}
