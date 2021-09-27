package songgrp.song.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.Authorization;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;
import songgrp.song.service.SongService;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/songs")
public class SongController extends Authorization {

    @Autowired
    private final SongService songService;

    public SongController(SongRepository songRepository) {
        this.songService = new SongService(songRepository);
    }

    // GET one song /songs/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object>
    getSong(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(value = "id") Integer id) {
        try {
            authorizeUser(authToken);
            return songService.getSong(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // GET one song /songs/1/lyric
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}/lyrics", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object>
    getLyricForSong(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(value = "id") Integer id) {
        try {
            authorizeUser(authToken);
            return songService.getLyricForSong(id, authToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // GET all songs /songs
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object>
    getAllSongs(
            @RequestHeader("Authorization") String authToken) {
        try {
            authorizeUser(authToken);
            return songService.getAllSong();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // POST new song /songs
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Song an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object>
    addSong(
            @RequestHeader("Authorization") String authToken,
            @RequestBody Song songToAdd) {
        try {
            authorizeUser(authToken);
            return songService.addSong(songToAdd);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // PUT update song /songs/1
    // Eingabeformat JSON
    // Update nur, wenn „songId“ in URL gleich „id“ in Payload „title“-Attribute darf nicht leer sein.
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object>
    updateSong(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(value = "id") Integer id,
            @RequestBody Song songToPut) {
        try {
            authorizeUser(authToken);
            return songService.updateSong(id, songToPut);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    // DELETE a song by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object>
    deleteSong(
            @RequestHeader("Authorization") String authToken,
            @PathVariable(value = "id") Integer id) {
        try {
            authorizeUser(authToken);
            return songService.deleteSong(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object>
    deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
