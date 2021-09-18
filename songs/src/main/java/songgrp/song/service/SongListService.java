package songgrp.song.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;

import java.net.URI;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class SongListService {

    private final SongListRepository songListRepository;

    public SongListService(SongListRepository songListRepository) {
        this.songListRepository = songListRepository;
    }


    public ResponseEntity<Object> getSongList(String userId, Integer id) {
        var val = songListRepository.findByIdOrderById(id);
        if (val.isEmpty()) return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        SongList requestedSongList = val.get();

        if (userId.equals(requestedSongList.getOwnerId())
                || !requestedSongList.isPrivate()) {
            return new ResponseEntity<Object>(requestedSongList, HttpStatus.OK);
        } else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND); //FORBIDDEN

    }

    public ResponseEntity<Object> getAllSongLists(String userId) {
        return new ResponseEntity<Object>(songListRepository.findByOwnerIdOrIsPrivateOrderById(userId, false), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSongList(String userId, SongList songListToAdd) {
        if (songListToAdd.getName() != null && !songListToAdd.getName().isEmpty()) {
            songListToAdd.setOwnerId(userId);
            SongList newSongList = songListRepository.save(songListToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songLists/" + newSongList.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateSongList(String userId, Integer id, SongList songListToPut) {
        SongList songListToUpdate = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        if (!songListToPut.getOwnerId().equals(userId)) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        if (songListToPut.getName() != null) {
            songListToUpdate.setName(songListToPut.getName());
        }
        if (songListToPut.getSongList() != null) {
            songListToUpdate.setSongList(songListToPut.getSongList());
        }
        songListToUpdate.setPrivate(songListToPut.isPrivate());

        return new ResponseEntity<Object>(songListRepository.save(songListToUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteSongList(String userId, Integer id) {
        SongList songList = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        if (!songList.getOwnerId().equals(userId)) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }


}