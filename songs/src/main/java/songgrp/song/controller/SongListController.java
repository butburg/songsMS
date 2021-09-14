package songgrp.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.Authorization;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;
import songgrp.song.service.SongListService;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songLists")
public class SongListController extends Authorization {

    @Autowired
    private final SongListService songListService;

    public SongListController(SongListRepository repo) {
        this.songListService = new SongListService(repo);
    }


    // GET one songlist http://localhost:8080/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSongList(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        try {
            return songListService.getSongList(id, authorizeUser(authToken));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }


    // GET all songlists http://localhost:8080/songLists
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getAllSongLists(@RequestHeader("Authorization") String authToken) {
        try {
            return songListService.getAllSongLists(authorizeUser(authToken));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // POST new songlist http://localhost:8080/songLists
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Songlist an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSongList(
            @RequestBody SongList songList,
            @RequestHeader("Authorization") String authToken) {
        try {
            return songListService.addSongList(songList, authorizeUser(authToken));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    //TODO Put song list


    // DELETE a songlist by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSongList(
            @PathVariable(value = "id") Integer id,
            @RequestHeader("Authorization") String authToken) {
        try {
            return songListService.deleteSongList(id, authorizeUser(authToken));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
