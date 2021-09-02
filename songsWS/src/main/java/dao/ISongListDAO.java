package dao;

import model.SongList;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Edwin W (HTW) on Jun 2021
 */

@Repository
public interface ISongListDAO{

    @Transactional
    Integer saveSongList(SongList songList) throws PersistenceException;

    SongList findSongList(Integer identifier);

    List<SongList> findAllSongListsOf(String owner);

    void deleteSongList(Integer id) throws PersistenceException;
}
