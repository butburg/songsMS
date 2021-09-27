package lyricgrp.controller;

import lyricgrp.service.LyricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/lyrics")
public class LyricController extends Authorization {

    @Autowired
    LyricService lyricService;

    public LyricController() {
        this.lyricService = new LyricService();
    }

    // GET one song /songs/1
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object>
    getLyric(
            @RequestHeader("Authorization") String authToken,
            @RequestHeader("Artist") String artistName,
            @RequestHeader("Song") String songName) {
        try {
            //authorizeUser(authToken);
            return lyricService.getLyric(artistName, songName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
