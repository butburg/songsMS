package authgrp.auth.controller;

import authgrp.auth.model.User;
import authgrp.auth.repo.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository repo) {
        this.userRepository = repo;
    }

    @PostMapping
    public ResponseEntity<Object> loginUser(@RequestBody User search) {

        String searchedUserId = search.getUserId();
        String searchedUserPw = search.getPassword();
        User user = userRepository.findByUserId(searchedUserId);

        if (user == null || user.getUserId() == null ||
                user.getPassword() == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getUserId().equals(searchedUserId) && user.getPassword().equals(searchedUserPw)) {
            String token = user.generateAndSetToken();
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.add("Authorization", token);
            userRepository.save(user);
            return new ResponseEntity<Object>(token, authHeader, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }
}