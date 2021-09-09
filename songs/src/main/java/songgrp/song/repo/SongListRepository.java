package songgrp.song.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import songgrp.song.model.Song;
import songgrp.song.model.SongList;

/**
 * @author github.com/butburg (EW) on Sep 2021
 *
 * like my dao, does the CRUD
 */
@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {
}
