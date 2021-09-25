package lyricsgrp.controller;

import lyricsgrp.service.LyricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/lyrics")
public class LyricsController {

    @Autowired
    LyricsService lyricsService;

    public LyricsController() {
        this.lyricsService = new LyricsService();
    }

    // GET one song /songs/1
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object>
    getLyrics(
            @RequestHeader("Authorization") String authToken) {
        try {

            return lyricsService.getLyrics("artistName", "songName");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

}
