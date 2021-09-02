package controller;

import dao.ISongDAO;
import dao.ISongListDAO;
import dao.IUserDAO;
import model.Song;
import model.SongList;
import model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Edwin W (HTW) on Mai 2021
 * Hier werden via DAO Interface die Songs je nach Anfrage verarbeitet sowie Responses geschickt.
 */
@RestController
@RequestMapping(value = "/songLists")
public class SongListControllerDI {

    //    @Autowired
    private ISongListDAO songListDAO;
    private ISongDAO songDAO;
    private IUserDAO userDAO;

    public SongListControllerDI(ISongListDAO slDAO, IUserDAO userDAO, ISongDAO songDAO) {
        this.songListDAO = slDAO;
        this.userDAO = userDAO;
        this.songDAO = songDAO;
    }

    private User tokenToAuthUser(String authToken) {
        List<User> users = userDAO.findAllUsers();
        User user = null;
        for (User u : users) {
            if (authToken.equals(u.getToken())) {
                user = u;
                break;
            }
        }
        return user;
    }

    //GET /rest/songLists?userId=SOME_USERID
    //GET /rest/songLists?userId=mmuster: soll alle Songlisten von mmuster an User mmuster zurückschicken.
    //GET /rest/songLists?userId=eschuler: soll nur die public Songlisten von ‘eschuler’ an User ‘mmuster’ zurückschicken.
    //GET /rest/songLists?userId=usergibtsnicht: schickt HTTP-Statuscode 404 zurück.
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSongListByUser(
            @RequestParam(required = false, name = "userId") String userId, @RequestHeader("Authorization") String authToken) {
        User owner = tokenToAuthUser(authToken);

        if (owner == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        if (userId == null) {
            List<SongList> songLists = songListDAO.findAllSongListsOf(owner.getUserId());
            if (songLists != null) {
                return new ResponseEntity<Object>(songLists, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }


        try {
            List<SongList> songLists = songListDAO.findAllSongListsOf(userId);
            if (!owner.getUserId().equals(userId)) {
                List<SongList> publicSongLists = new ArrayList<>();
                for (SongList sl : songLists) {
                    if (!sl.isPrivate()) {
                        publicSongLists.add(sl);
                    }
                }
                songLists = publicSongLists;
                if (songLists.isEmpty()) {
                    return new ResponseEntity<Object>(songLists, HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<Object>(songLists, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }


    // GET http://localhost:8080/songsWS-sevenfour/rest/songLists/1
    // Ausgabeformat JSON und XML
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSongList(
            @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        User owner = tokenToAuthUser(authToken);
        if (owner == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        SongList songList = songListDAO.findSongList(id);
        if (songList != null) {
            if (owner.getUserId().equals(songList.getOwnerId()) || !songList.isPrivate()) {
                return new ResponseEntity<Object>(songList, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }

    // POST
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein
    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSongList(
            @RequestBody SongList songList, @RequestHeader("Authorization") String authToken) {
        User owner = tokenToAuthUser(authToken);
        if (owner == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        if (songList.getName() != null && !songList.getName().isEmpty()) {
            songList.setOwnerId(owner.getUserId());
            if (songList.getSongList() != null) {
                Set<Song> songs = songList.getSongList();
                Set<Song> foundedSongs = new HashSet<Song>();
                for (Song x : songs) {
                    try {
                        if (x.toString().equals(songDAO.findSong(x.getId()).toString())) {
                            foundedSongs.add(songDAO.findSong(x.getId()));
                        } else return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
                    } catch (NullPointerException e) {
                        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
                    }
                }
                songList.setSongList(foundedSongs);
            }
            Integer id = songListDAO.saveSongList(songList);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/rest/songLists/" + id));
            return new ResponseEntity<Object>(songList, header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // Eingabeformat JSON
    // Update nur, wenn songList id in URL gleich „id“ in Payload
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404

    @PostMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> updateSongList(@RequestBody SongList songList, @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        if (id.equals(songList.getId())) {
            try {
                songListDAO.updateSongList(songList);
                return new ResponseEntity<Object>(songList, HttpStatus.NO_CONTENT);
            } catch (EntityNotFoundException ex) {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    // wenn SongList vorhanden, autorisiert ist und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSongList(
            @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        User owner = tokenToAuthUser(authToken);
        if (owner == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        SongList songList = songListDAO.findSongList(id);
        if (songList != null) {
            if (owner.getUserId().equals(songList.getOwnerId())) {
                songListDAO.deleteSongList(id);
                return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Song> deleteWrongPath() {
        return new ResponseEntity<Song>(HttpStatus.BAD_REQUEST);
    }
}
