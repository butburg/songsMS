package controller;

import dao.ISongDAO;
import dao.IUserDAO;
import model.Song;
import model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Edwin W (HTW) on Mai 2021
 * Hier werden via DAO Interface die Songs je nach Anfrage verarbeitet sowie Responses geschickt.
 */
@RestController
@RequestMapping(value = "/songs")
public class SongControllerDI {

    //    @Autowired
    private ISongDAO songDAO;
    private IUserDAO userDAO;

    public SongControllerDI(ISongDAO sDAO, IUserDAO userDAO) {
        this.songDAO = sDAO;
        this.userDAO = userDAO;
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

    // GET http://localhost:8080/songsWS-butburg/rest/songs/1
    // Ausgabeformat JSON und XML
    // rest/songs/1 schickt Song mit id = 1 zurück, 1 <= id <= n
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getSong(
            @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) throws IOException {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        Song song = songDAO.findSong(id);
        if (song != null) {
            return new ResponseEntity<Object>(song, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);

    }

    // GET http://localhost:8080/songsWS-butburg/rest/songs
    // Ausgabeformat JSON und XML
    // rest/songs schickt alle Songs zurück
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAllSongs(@RequestHeader("Authorization") String authToken) {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<List<Song>>(HttpStatus.UNAUTHORIZED);
        }
        List<Song> songs = songDAO.findAllSongs();
        if (!songs.isEmpty()) {
            return new ResponseEntity<List<Song>>(songs, HttpStatus.OK);
        }
        return new ResponseEntity<List<Song>>(HttpStatus.NOT_FOUND);
    }

    // POST
    // Eingabeformat JSON
    // „title“-Attribute darf nicht leer sein
    // wenn erfolgreich, legt neuen Song an,
    // schickt Statuscode 201 und URI (/rest/songs/) zur neuen Ressource im „Location“-Header zurück
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addSong(@RequestBody Song song, @RequestHeader("Authorization") String authToken) {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        if (song.getTitle() != null && !song.getTitle().isEmpty()) {
            Integer id = songDAO.saveSong(song);
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create("/rest/songs/" + id));
            return new ResponseEntity<Object>(song, header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUT
    // Eingabeformat JSON
    // Update nur, wenn „songId“ in URL gleich „id“ in Payload(
    // „title“-Attribute darf nicht leer sein.
    // Wenn Update erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateSong(@RequestBody Song song, @PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        if (song.getTitle() != null && !song.getTitle().isEmpty() && id.equals(song.getId())) {
            try {
                songDAO.updateSong(song);
                return new ResponseEntity<Object>(song, HttpStatus.NO_CONTENT);
            } catch (EntityNotFoundException ex) {
                return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    // wenn Song vorhanden und Löschen erfolgreich, dann nur Statuscode 204 zurückschicken, ansonsten 400 bzw. 404
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteSong(@PathVariable(value = "id") Integer id, @RequestHeader("Authorization") String authToken) {
        if (tokenToAuthUser(authToken) == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
        Song song = songDAO.findSong(id);
        if (song != null) {
            songDAO.deleteSong(id);
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Song> deleteWrongPath() {
        return new ResponseEntity<Song>(HttpStatus.BAD_REQUEST);
    }
}
