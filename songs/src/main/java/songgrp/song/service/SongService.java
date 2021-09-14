package songgrp.song.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public ResponseEntity<Object> getSong(Integer id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        return new ResponseEntity<Object>(song, HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllSong() {
        return new ResponseEntity<Object>(songRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSong(Song songToAdd) {
        if (songToAdd.getTitle() != null && !songToAdd.getTitle().isEmpty()) {
            Song newSong = songRepository.save(songToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songs/" + newSong.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateSong(Integer id, Song songToPut) {
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
            return new ResponseEntity<Object>(songRepository.save(song), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> deleteSong(Integer id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        songRepository.delete(song);
        return ResponseEntity.noContent().build();
    }
}