package songgrp.song.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songLists")
public class SongListController {

    private final SongListRepository songListRepository;

    public SongListController(SongListRepository repo) {
        this.songListRepository = repo;
    }

    // GET one songlist http://localhost:8080/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SongList getSongList(@PathVariable(value = "id") Integer id) {
        return songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
    }

    // GET all songlists http://localhost:8080/songLists
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Iterable<SongList> getAllSongLists() {
        return songListRepository.findAll();
    }

    // POST new songlist http://localhost:8080/songLists
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Songlist an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSongList(@RequestBody SongList songList) {
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
    public ResponseEntity<Object> deleteSongList(@PathVariable(value = "id") Integer id) {
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
