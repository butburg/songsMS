package lyricsgrp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */
public abstract class Authorization {

    @Autowired
    RestTemplate restTemplate;

    public String authorizeUser(String authToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://auth/auth", HttpMethod.GET, entity, String.class).getBody();
    }
}

