package lyricgrp.service;

import lyricgrp.model.Lyric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class LyricService {

    @Autowired
    RestTemplate getRestTemplate;
    @Value("${api.root_domain}")
    private String rootDomain;

    public ResponseEntity<Object> getLyric(String artistName, String songName) {
        //build proper url format
        String artistNameUrl = URLEncoder.encode(artistName, StandardCharsets.UTF_8);
        String songNameUrl = URLEncoder.encode(songName, StandardCharsets.UTF_8);
        String url = rootDomain + "SearchLyricDirect?artist=" + artistNameUrl + "&Song=" + songNameUrl + "";

        System.out.println("Access: " + url);
        Lyric lyricsResponse = getRestTemplate.getForObject(
                url
                , Lyric.class);
        if (lyricsResponse != null && lyricsResponse.getLyricId() != 0) {
            return new ResponseEntity<>(lyricsResponse, HttpStatus.OK);
        } else
            return new ResponseEntity<>("No results by: " + url, HttpStatus.NOT_FOUND);
    }

}