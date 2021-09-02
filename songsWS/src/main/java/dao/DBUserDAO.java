package dao;

import model.User;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

public class DBUserDAO implements IUserDAO {

    private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
    private EntityManagerFactory emf;

    public DBUserDAO() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public DBUserDAO(EntityManagerFactory emf){this.emf = emf;}

    @Transactional
    @Override
    public String saveUser(User user) throws PersistenceException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user.getUserId();
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
    public User findUser(String userId) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT user FROM User user WHERE user.id = '" + userId + "'"); //JPQL
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<User> findAllUsers() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT user FROM User user"); //JPQL
            return (List<User>) q.getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void updateToken(User user) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            User userNew = findUser(user.getUserId());
            if (userNew != null) {
                if (user.getToken() != null) {
                    userNew.setToken(user.getToken());
                }
                em = emf.createEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                em.merge(userNew);
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
    public void updateUser(User user) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            User userNew = findUser(user.getUserId());
            if (userNew != null) {
                if (user.getLastName() != null) {
                    userNew.setLastName(user.getLastName());
                }
                if (user.getFirstName() != null) {
                    userNew.setFirstName(user.getFirstName());
                }
                em = emf.createEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                em.merge(userNew);
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
    public void deleteUser(String userId) throws PersistenceException {
        EntityManager em = null;
        em = emf.createEntityManager();
        User user = null;
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            // find User with id
            user = em.find(User.class, userId);
            if (user != null) {
                System.out.println("Deleting: " + user.getUserId());
                em.remove(user);
                transaction.commit();
            }
        } catch (Exception e) {
            System.out.println("Error removing user: " + e.getMessage());
            throw new PersistenceException("Could not remove entity: " + e.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

