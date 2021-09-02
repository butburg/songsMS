package dao;

import model.Song;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Hier werden via DAO Interface die Songs in der Datenbank verwaltet. Die Methoden werden vom Controller aufgerufen.
 */
public class DBSongDAO implements ISongDAO {

    private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
    private EntityManagerFactory emf;

    public DBSongDAO() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public DBSongDAO(EntityManagerFactory emf){this.emf = emf;}

    @Transactional
    @Override
    public Integer saveSong(Song song) throws PersistenceException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(song);
            em.flush();
            em.getTransaction().commit();
            return song.getId();
        } catch (IllegalStateException | EntityExistsException | RollbackException ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException(ex.getMessage());
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Song findSong(Integer identifier) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT song FROM Song song WHERE song.id = " + identifier); //JPQL
            return (Song) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Song> findAllSongs() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT song FROM Song song"); //JPQL
            return (List<Song>) q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    @Override
    public void updateSong(Song song) throws EntityNotFoundException {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            Song songNew = findSong(song.getId());
            if (songNew != null) {
                songNew.setTitle(song.getTitle());
                if (song.getArtist() != null) {
                    songNew.setArtist(song.getArtist());
                }
                if (song.getLabel() != null) {
                    songNew.setLabel(song.getLabel());
                }
                if (song.getReleased() != null) {
                    songNew.setReleased(song.getReleased());
                }
                em = emf.createEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                em.merge(songNew);
                transaction.commit();
            } else {
                throw new EntityNotFoundException();
            }
        } catch (IllegalStateException | RollbackException ex) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void deleteSong(Integer id) throws PersistenceException {
        EntityManager em = null;
        em = emf.createEntityManager();
        Song song = null;
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            // find User with id
            song = em.find(Song.class, id);
            if (song != null) {
                em.remove(song);
                transaction.commit();
            }
        } catch (Exception e) {
            System.out.println("Error removing song: " + e.getMessage());
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
