package lyricsgrp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lyricsgrp.model.LyricsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class LyricsService {


    @Value("${api.root_domain}")
    private String rootDomain;
    @Value("${api.key}")
    private String apikey;

    @Autowired
    RestTemplate getRestTemplate;


    public ResponseEntity<Object> getLyrics(String ArtistName, String SongName) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept","application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        LyricsResponse lyricsResponse = getRestTemplate.exchange(
                "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist=michael%20jackson&song=bad"
                ,HttpMethod.GET, entity ,LyricsResponse.class).getBody();

        //hier soll json in die Entitys umgewandelt werden, erst in Message, darin dann in Header und Body und in Body ist die entity Lyrics drin
        //ObjectMapper mapper = new ObjectMapper();

        //LyricsResponse messageList = mapper.readValue(lyricsResponse, LyricsResponse.class);
        System.out.println(lyricsResponse.toString());

        /*LyricsResponse lyricsResponse = getRestTemplate.getForObject(
                "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=sexy%20and%20i%20know%20it&q_artist=lmfao&apikey=3983b4ca9fdc2539538c0a676518ee2a", LyricsResponse.class);
        System.out.println(lyricsResponse.toString());
        */

        return new ResponseEntity<>(lyricsResponse.getLyric(), HttpStatus.OK); //TODO getLyrics_body


/*
        String variable = restTemplate.exchange(
                "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=sexy%20and%20i%20know%20it&q_artist=lmfao&apikey=3983b4ca9fdc2539538c0a676518ee2a", HttpMethod.GET, entity, String.class).getBody();
*/

    }


}