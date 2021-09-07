package songgrp.song.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songgrp.song.exception.BadRequestException;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;

import java.net.URI;

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
    public Song getSong(@PathVariable(value = "id") Integer id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
    }

    // GET all songs http://localhost:8080/songs
    // Ausgabeformat JSON und XML
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Iterable<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // POST new song http://localhost:8080/songs
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein, wenn erfolgreich, legt neuen Song an,
    // schickt Statuscode 201 und URI (/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSong(@RequestBody Song song) {
        if (song.getTitle() != null && !song.getTitle().isEmpty()) {
            Song newSong = songRepository.save(song);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songs/" + newSong.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT update song http://localhost:8080/songs/1
    // Eingabeformat JSON
    // Update nur, wenn „songId“ in URL gleich „id“ in Payload „title“-Attribute darf nicht leer sein.
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @PutMapping(value = "/{id}")
    public Song updateSong(@RequestBody Song songToPut, @PathVariable(value = "id") Integer id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));

        if (songToPut.getTitle() != null && !songToPut.getTitle().isEmpty()) {
            song.setTitle(songToPut.getTitle());

            if (songToPut.getArtist() != null) {
                song.setArtist(songToPut.getArtist());
            }
            if (songToPut.getLabel() != null) {
                song.setLabel(songToPut.getLabel());
            }
            if (songToPut.getReleased() != null) {
                song.setReleased(songToPut.getReleased());
            }
            return songRepository.save(song);
        } else {
            throw new BadRequestException("Song", "id", id);
        }
    }

    // DELETE a song by id
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSong(@PathVariable(value = "id") Integer id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        songRepository.delete(song);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWrongPath() {
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
