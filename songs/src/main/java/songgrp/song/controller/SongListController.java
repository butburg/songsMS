package songgrp.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.Authorization;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;
import songgrp.song.repo.SongRepository;
import songgrp.song.service.SongListService;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songLists")
public class SongListController extends Authorization {

    @Autowired
    private final SongListService songListService;


    public SongListController(SongListRepository repo, SongRepository songRepo) {
        this.songListService = new SongListService(repo, songRepo);
    }


    // GET one songlist http://localhost:8080/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSongList(
            @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        try {
            return songListService.getSongList(authorizeUser(authToken), id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getAllSongListsByUserId(
            @RequestHeader("Authorization") String authToken,
            @RequestParam(required = false, name = "userId") String userIdSearch) {
        try {
            if(userIdSearch == null){
                System.out.println("null");
                return songListService.getAllSongLists(authorizeUser(authToken));
            }
            return songListService.getSongListByUserId(authorizeUser(authToken), userIdSearch);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
            return songListService.addSongList(authorizeUser(authToken), songList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object>
    updateSongList(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(value = "id") Integer id,
            @RequestBody SongList songListToPut) {
        try {
            return songListService.updateSongList(authorizeUser(authToken), id, songListToPut);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // DELETE a songlist by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSongList(
            @PathVariable(value = "id") Integer id,
            @RequestHeader("Authorization") String authToken) {
        try {
            return songListService.deleteSongList(authorizeUser(authToken), id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
