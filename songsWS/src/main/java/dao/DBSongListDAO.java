package dao;

import model.SongList;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Hier werden via DAO Interface die Songs in der Datenbank verwaltet. Die Methoden werden vom Controller aufgerufen.
 */
public class DBSongListDAO implements ISongListDAO {

    private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
    private EntityManagerFactory emf;

    public DBSongListDAO() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public DBSongListDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Transactional
    @Override
    public Integer saveSongList(SongList songList) throws PersistenceException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(songList);
            em.flush();
            em.getTransaction().commit();
            return songList.getId();
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
    public SongList findSongList(Integer identifier) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT songList FROM SongList songList WHERE songList.id = " + identifier); //JPQL
            return (SongList) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<SongList> findAllSongListsOf(String owner) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT songList FROM SongList songList WHERE ownerId LIKE '" + owner + "'"); //JPQL
            return (List<SongList>) q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    @Override
    public void updateSongList(SongList songList) throws EntityNotFoundException {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            SongList songListNew = findSongList(songList.getId());
            if (songListNew != null) {
                songListNew.setPrivate(songList.isPrivate());
                if (songList.getName() != null) {
                    songListNew.setName(songList.getName());
                }
                if (songList.getSongList() != null) {
                    songListNew.setSongList(songList.getSongList());
                }

                em = emf.createEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                em.merge(songListNew);
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
    public void deleteSongList(Integer id) throws PersistenceException {
        EntityManager em = null;
        em = emf.createEntityManager();
        SongList songList = null;
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            // find User with id
            songList = em.find(SongList.class, id);
            if (songList != null) {
                System.out.println("Deleting: " + songList.getId() + " with titel: " + songList.getName());
                em.remove(songList);
                transaction.commit();
            }
        } catch (Exception e) {
            System.out.println("Error removing song: " + e.getMessage());
            throw new PersistenceException("Could not remove entity: " + e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
