package authgrp.auth.repo;

import authgrp.auth.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author github.com/butburg (EW) on Sep 2021
 * <p>
 * like my dao, does the CRUD
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserId(String id);
}
