package lyricsgrp.service;

import lyricsgrp.model.Lyrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class LyricsService {

    String rootDomain ="https://api.musixmatch.com/ws/1.1/";
    String apikey ="3983b4ca9fdc2539538c0a676518ee2a";


    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<Object> getLyrics(String ArtistName,String SongName) {
        System.out.println("POPEL");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String variable = restTemplate.exchange(
                "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=sexy%20and%20i%20know%20it&q_artist=lmfao&apikey=3983b4ca9fdc2539538c0a676518ee2a", HttpMethod.GET, entity, String.class).getBody();
        return new ResponseEntity<>(variable, HttpStatus.OK); //TODO getLyrics_body

    }


}