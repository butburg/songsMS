package songgrp.song.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import songgrp.song.exception.ResourceNotFoundException;
import songgrp.song.model.SongList;
import songgrp.song.model.User;
import songgrp.song.repo.SongListRepository;
import songgrp.song.repo.SongRepository;

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


    public ResponseEntity<Object> getSongList(Integer id, User aUser) {
        SongList requestedSongList = songListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song", "id", id));
        if (aUser.getUserId().equals(requestedSongList.getOwnerId())
                || !requestedSongList.isPrivate()) {
            return new ResponseEntity<Object>(requestedSongList, HttpStatus.OK);
        } else return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);

    }

    public ResponseEntity<Object> getAllSongLists(User aUser) {
        //TODO check which songlists for user are visible
        return new ResponseEntity<Object>(songListRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSongList(SongList songListToAdd, User user) {
        //TODO check which user added the songlist
        if (songListToAdd.getName() != null && !songListToAdd.getName().isEmpty()) {
            SongList newSongList = songListRepository.save(songListToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songLists/" + newSongList.getId()));
            return new ResponseEntity<Object>(header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }


    /*public ResponseEntity<Object> updateSongList(Integer id, SongList songListToPut) {
        SongList songList = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SongList", "id", id));
        if (songListToPut.getTitle() != null && !songListToPut.getTitle().isEmpty()) {
            songList.setTitle(songListToPut.getTitle());

            if (songListToPut.getArtist() != null) {
                songList.setArtist(songListToPut.getArtist());
            }
            if (songListToPut.getLabel() != null) {
                songList.setLabel(songListToPut.getLabel());
            }
            if (songListToPut.getReleased() != null) {
                songList.setReleased(songListToPut.getReleased());
            }
            return new ResponseEntity<Object>(songListRepository.save(songList), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }*/

    public ResponseEntity<Object> deleteSongList(Integer id, User user) {
        //TODO only user can delete his own list!
        SongList songList = songListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Songlist", "id", id));
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }
}