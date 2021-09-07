package songgrp.song.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;

import java.io.IOException;
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

    // GET http://localhost:8080/songsWS-butburg/rest/songs/1
    // Ausgabeformat JSON und XML
    // rest/songs/1 schickt Song mit id = 1 zurück, 1 <= id <= n
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSong(
            @PathVariable(value = "id") Integer id) throws IOException {

        Song song = songRepository.findById(id).get();
        return new ResponseEntity<Object>(song, HttpStatus.OK);
    }

    // GET http://localhost:8080/songsWS-butburg/rest/songs
    // Ausgabeformat JSON und XML
    // rest/songs schickt alle Songs zurück
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAllSongs() {

        List<Song> songs = (List) songRepository.findAll();

        return new ResponseEntity<List<Song>>(songs, HttpStatus.OK);

    }

    // POST
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein
    // wenn erfolgreich, legt neuen Song an,
    // schickt Statuscode 201 und URI (/rest/songs/) zur neuen Ressource im „Location“-Header zurück

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSong(@RequestBody Song song) {
        if (song.getTitle() != null && !song.getTitle().isEmpty()) {
            Integer id = songRepository.save(song).getId();
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/rest/songs/" + id));
            return new ResponseEntity<Object>(song, header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // Eingabeformat JSON
    // Update nur, wenn „songId“ in URL gleich „id“ in Payload(
    // „title“-Attribute darf nicht leer sein.
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<Object> updateSong(@RequestBody Song song, @PathVariable(value = "id") Integer id) {
//
//        if (song.getTitle() != null && !song.getTitle().isEmpty() && id.equals(song.getId())) {
//            try {
//                songDAO.updateSong(song);
//                return new ResponseEntity<Object>(song, HttpStatus.NO_CONTENT);
//            } catch (EntityNotFoundException ex) {
//                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
//            }
//        } else {
//            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // DELETE
//    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<Object> deleteSong(@PathVariable(value = "id") Integer id) {
//
//        Song song = songDAO.findSong(id);
//        if (song != null) {
//            songDAO.deleteSong(id);
//            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Song> deleteWrongPath() {
//        return new ResponseEntity<Song>(HttpStatus.BAD_REQUEST);
//    }
}
