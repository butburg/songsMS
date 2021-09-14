package songgrp.song.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.SongList;
import songgrp.song.model.User;
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


    public ResponseEntity<Object> getSongList(User user, Integer id) {
        SongList requestedSongList = songListRepository.findByIdAndOwnerIdOrIsPrivate(id,user.getUserId(),false)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        if (user.getUserId().equals(requestedSongList.getOwnerId())
                || !requestedSongList.isPrivate()) {
            return new ResponseEntity<Object>(requestedSongList, HttpStatus.OK);
        } else return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);

    }

    public ResponseEntity<Object> getAllSongLists(User user) {
        return new ResponseEntity<Object>(songListRepository.findByOwnerIdOrIsPrivate(user.getUserId(), false), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSongList(User user, SongList songListToAdd) {
        if (songListToAdd.getName() != null && !songListToAdd.getName().isEmpty()) {
            songListToAdd.setOwnerId(user.getUserId());
            SongList newSongList = songListRepository.save(songListToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songLists/" + newSongList.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateSongList(User user, Integer id, SongList songListToPut) {
        SongList songListToUpdate = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        if (!songListToPut.getOwnerId().equals(user.getUserId())) {
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

    public ResponseEntity<Object> deleteSongList(User user, Integer id) {
        SongList songList = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        if (!songList.getOwnerId().equals(user.getUserId())) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }


}