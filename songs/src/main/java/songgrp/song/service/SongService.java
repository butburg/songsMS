package songgrp.song.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import songgrp.song.model.Lyric;
import songgrp.song.model.Song;
import songgrp.song.repo.SongRepository;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class SongService {

    private final SongRepository songRepository;
    @Autowired
    RestTemplate restTemplate;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public ResponseEntity<Object> getSong(Integer id) {
        var song = songRepository.findById(id);
        if (song.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(song.get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> getLyricForSong(Integer id, String authToken) {
        var song = songRepository.findById(id);
        if (song.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        System.out.println(song.get());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);
        headers.set("Accept", "application/json");
        headers.set("Artist", song.get().getArtist());
        headers.set("Song", song.get().getTitle());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            Lyric lyric = restTemplate.exchange(
                    "http://lyrics-service/lyrics", HttpMethod.GET, entity, Lyric.class).getBody();
            return new ResponseEntity<>(lyric, HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException exc) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            return new ResponseEntity<>(
                    "No matching lyrics found for: " + song.get().getTitle(),
                    responseHeaders,
                    exc.getStatusCode());
        }
    }

    public ResponseEntity<Object> getAllSong() {
        return new ResponseEntity<>(songRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSong(Song songToAdd) {
        if (songToAdd.getTitle() != null && !songToAdd.getTitle().isEmpty()) {
            Song newSong = songRepository.save(songToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songs/" + newSong.getId()));
            return new ResponseEntity<>(songToAdd, header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> updateSong(Integer id, Song songToPut) {
        var val = songRepository.findById(id);
        if (val.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> deleteSong(Integer id) {
        var song = songRepository.findById(id);
        if (song.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            songRepository.delete(song.get());
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        return ResponseEntity.noContent().build();
    }

}