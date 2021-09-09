package songgrp.song.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import songgrp.song.model.Song;

/**
 * @author github.com/butburg (EW) on Sep 2021
 * <p>
 * like my dao, does the CRUD
 */
@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {
}
