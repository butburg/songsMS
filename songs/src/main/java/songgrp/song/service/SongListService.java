package songgrp.song.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import songgrp.song.model.Song;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;
import songgrp.song.repo.SongRepository;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@Service
public class SongListService {

    private final SongListRepository songListRepository;
    private final SongRepository songRepository;

    public SongListService(SongListRepository songListRepository, SongRepository songRepository) {
        this.songListRepository = songListRepository;
        this.songRepository = songRepository;
    }


    public ResponseEntity<Object> getSongList(String userId, Integer id) {
        var val = songListRepository.findByIdOrderById(id);
        if (val.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        SongList requestedSongList = val.get();

        if (userId.equals(requestedSongList.getOwnerId())
                || !requestedSongList.isPrivate()) {
            return new ResponseEntity<>(requestedSongList, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND); //FORBIDDEN

    }

    public ResponseEntity<Object> getSongListByUserId(String userId, String userIdSearch) {
        if (userId.equals(userIdSearch)) {
            return new ResponseEntity<>(songListRepository.findByOwnerIdOrderById(userIdSearch), HttpStatus.OK);
        } else if (!userIdSearch.isEmpty()) {
            return new ResponseEntity<>(songListRepository.findByOwnerIdAndIsPrivateOrderById(userIdSearch, false), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> getAllSongLists(String userId) {
        return new ResponseEntity<>(songListRepository.findByOwnerIdOrIsPrivateOrderById(userId, false), HttpStatus.OK);
    }

    public ResponseEntity<Object> addSongList(String userId, SongList songListToAdd) {
        if (songListToAdd.getName() == null || songListToAdd.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        songListToAdd.setOwnerId(userId);
        try {
            SongList newSongList = songListRepository.save(songListToAdd);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/songLists/" + newSongList.getId()));
            return new ResponseEntity<>(newSongList, header, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateSongList(String userId, Integer id, SongList songListToPut) {
        if (!songListToPut.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var val = songListRepository.findById(id);
        if (val.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        SongList songListToUpdate = val.get();

        System.out.println(songListToUpdate + " null? " + songListToPut);

        if (!songListToUpdate.getOwnerId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (songListToPut.getName() != null && !songListToPut.getName().isEmpty()) {
            songListToUpdate.setName(songListToPut.getName());
        }

        if (!songListToPut.getSongList().isEmpty()) {
            //custom list process follows here
            // default would be: songListToUpdate.setSongList(songListToPut.getSongList());
            Set<Song> songList = new HashSet<>();
            for (Song song : songListToPut.getSongList()) {
                try {
                    if (songRepository.findById(song.getId()).isPresent()) {
                        songList.add(songRepository.findById(song.getId()).get());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            songListToUpdate.setSongList(songList);
            //end
        }
        songListToUpdate.setPrivate(songListToPut.isPrivate());

        return new ResponseEntity<>(songListRepository.save(songListToUpdate), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteSongList(String userId, Integer id) {
        var val = songListRepository.findById(id);
        if (val.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        SongList songList = val.get();

        if (!songList.getOwnerId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        songListRepository.delete(songList);
        return ResponseEntity.noContent().build();
    }
}