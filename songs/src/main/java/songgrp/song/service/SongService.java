package songgrp.song.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        var song = songRepository.findById(id);
        if (song.isEmpty()) return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(song.get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllSong() {
        return new ResponseEntity<Object>(songRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSong(Song songToAdd) {
        if (songToAdd.getTitle() != null && !songToAdd.getTitle().isEmpty()) {
            Song newSong = songRepository.save(songToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songs/" + newSong.getId()));
            return new ResponseEntity<Object>(songToAdd, header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> updateSong(Integer id, Song songToPut) {
        var val = songRepository.findById(id);
        if (val.isEmpty()) return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        Song song = val.get();

        if (songToPut.getTitle() != null && !songToPut.getTitle().isEmpty() && songToPut.getId().equals(id)) {
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
            songRepository.save(song);

            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songs/" + id));
            return new ResponseEntity<Object>(header, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> deleteSong(Integer id) {
        var song = songRepository.findById(id);
        if (song.isEmpty()) return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        songRepository.delete(song.get());
        return ResponseEntity.noContent().build();
    }
}