package songgrp.song.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.List;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongRepository songRepository;

    public SongController(SongRepository repo) {
        this.songRepository = repo;
    }

    // GET one song http://localhost:8080/songs/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSong(@PathVariable(value = "id") Integer id) {
        try {
            Song song = songRepository.findById(id).get();
            return new ResponseEntity<Object>(song, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }

    // GET all songs http://localhost:8080/songs
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAllSongs() {
        try {
            List<Song> songs = (List) songRepository.findAll();
            return new ResponseEntity<List<Song>>(songs, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<List<Song>>(HttpStatus.NOT_FOUND);
        }
    }

    // POST new song http://localhost:8080/songs
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Song an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSong(@RequestBody Song song) {
        if (song.getTitle() != null && !song.getTitle().isEmpty()) {
            try {
                Integer id = songRepository.save(song).getId();
                HttpHeaders header = new HttpHeaders();
                header.setLocation(URI.create("/songs/" + id));
                return new ResponseEntity<Object>(song, header, HttpStatus.CREATED);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT update song http://localhost:8080/songs/1
    // Eingabeformat JSON
    // Update nur, wenn „songId“ in URL gleich „id“ in Payload „title“-Attribute darf nicht leer sein.
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateSong(@RequestBody Song song, @PathVariable(value = "id") Integer id) {
        if (song.getTitle() != null && !song.getTitle().isEmpty() && id.equals(song.getId())) {
            try {
                songRepository.save(song);
                return new ResponseEntity<Object>(song, HttpStatus.NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE a song by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSong(@PathVariable(value = "id") Integer id) {
        try {
            songRepository.deleteById(id);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
