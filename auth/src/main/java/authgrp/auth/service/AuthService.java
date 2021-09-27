package authgrp.auth.service;

import authgrp.auth.model.User;
import authgrp.auth.repo.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> checkUser(String authToken) {
        User user = userRepository.findByTokenLike(authToken);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user.getUserId(), HttpStatus.OK);
    }

    public ResponseEntity<Object> loginUser(User searchedUser) {
        String searchedUserId = searchedUser.getUserId();
        String searchedUserPw = searchedUser.getPassword();
        User user = userRepository.findByUserId(searchedUserId);

        if (user == null || user.getUserId() == null ||
                user.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getUserId().equals(searchedUserId) && user.getPassword().equals(searchedUserPw)) {
            String token = user.generateAndSetToken();
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.add("Authorization", token);
            userRepository.save(user);
            return new ResponseEntity<>(token, authHeader, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
