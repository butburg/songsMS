package songgrp.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import songgrp.song.exception.UnauthorizedException;
import songgrp.song.model.SongList;
import songgrp.song.model.User;
import songgrp.song.repo.SongRepository;
import songgrp.song.service.SongListService;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songLists")
public class SongListController {

    @Autowired
    private final SongListService songListService;

    public SongListController(SongRepository repo) {
        this.songListService = new SongListService(repo);
    }

    private User authorizeUser(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                "http://auth/auth", HttpMethod.GET, entity, User.class);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }


    // GET one songlist http://localhost:8080/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSongList(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        User aUser = authorizeUser(authToken);
        if (aUser == null) {
            throw new UnauthorizedException("Songlist", "id", id);
        }
        return songListService.getSongList(id, aUser);
    }


    // GET all songlists http://localhost:8080/songLists
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getAllSongLists(@RequestHeader("Authorization") String authToken) {
        User aUser = authorizeUser(authToken);
        if (aUser == null) {
            throw new UnauthorizedException("Songlist", "id", 0);
        }

        return songListService.getAllSongLists(aUser);

    }

    // POST new songlist http://localhost:8080/songLists
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Songlist an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSongList(
            @RequestBody SongList songList,
            @RequestHeader("Authorization") String authToken) {
        User aUser = authorizeUser(authToken);
        if (aUser == null) {
            throw new UnauthorizedException("Songlist", "id", 0);
        }
        return songListService.addSongList(songList);
    }

    //TODO Put song list


    // DELETE a songlist by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSongList(
            @PathVariable(value = "id") Integer id,
            @RequestHeader("Authorization") String authToken) {
        User aUser = authorizeUser(authToken);
        if (aUser == null) {
            throw new UnauthorizedException("Songlist", "id", id);
        }
        return songListService.deleteSongList(id);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
