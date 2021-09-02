package dao;

import model.Song;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Edwin W (HTW) on Mai 2021
 */

@Repository
public interface ISongDAO {

    @Transactional
    Integer saveSong(Song song) throws PersistenceException;

    Song findSong(Integer identifier);

    List<Song> findAllSongs();

    void updateSong(Song song);

    void deleteSong(Integer id) throws PersistenceException;
}
