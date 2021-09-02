package dao;

import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IUserDAO {

    @Transactional
    String saveUser(User user) throws PersistenceException;

    User findUser(String userId);

    List<User> findAllUsers();

    void updateUser(User user);

    void deleteUser(String userId) throws PersistenceException;

    void updateToken(User user);

}
