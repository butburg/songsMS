package songgrp.song.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.exception.UnauthorizedException;
import songgrp.song.model.SongList;
import songgrp.song.model.User;
import songgrp.song.repo.SongListRepository;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songLists")
public class SongListController {

    private final SongListRepository songListRepository;

    //@Autowired
    //private RestTemplate restTemplate;

    public SongListController(SongListRepository repo) {
        this.songListRepository = repo;
    }

    private User authorizeUser(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<User> response = restTemplate.exchange(
                "http://localhost:8081/auth", HttpMethod.GET, entity, User.class);
        return response.getBody();
    }


    // GET one songlist http://localhost:8080/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SongList getSongList(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        User AUser = authorizeUser(authToken);
        if (AUser == null) {
            throw new UnauthorizedException("Songlist", "id", id);
        }

        //TODO nur erlaubte songsLists anzeigen, in allen Methoden integrieren
        return songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
    }


    // GET all songlists http://localhost:8080/songLists
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Iterable<SongList> getAllSongLists(@RequestHeader("Authorization") String authToken) {
        User AUser = authorizeUser(authToken);
        if (AUser == null) {
            throw new UnauthorizedException("Songlist", "id", 0);
        }
        return songListRepository.findAll();
    }

    // POST new songlist http://localhost:8080/songLists
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Songlist an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSongList(
            @RequestBody SongList songList,
            @RequestHeader("Authorization") String authToken) {
        User AUser = authorizeUser(authToken);
        if (AUser == null) {
            throw new UnauthorizedException("Songlist", "id", 0);
        }
        if (songList.getName() != null && !songList.getName().isEmpty()) {
            SongList newSongList = songListRepository.save(songList);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songLists/" + newSongList.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE a songlist by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSongList(
            @PathVariable(value = "id") Integer id,
            @RequestHeader("Authorization") String authToken) {
        User AUser = authorizeUser(authToken);
        if (AUser == null) {
            throw new UnauthorizedException("Songlist", "id", id);
        }
        SongList songList = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
