package authgrp.auth.controller;

import authgrp.auth.model.User;
import authgrp.auth.repo.UserRepository;
import authgrp.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private final AuthService authService;

    public UserController(UserRepository repo) {
        this.authService = new AuthService(repo);
    }

    @GetMapping
    public ResponseEntity<Object> checkUser(@RequestHeader("Authorization") String authToken) {
        return authService.checkUser(authToken);
    }

    @PostMapping
    public ResponseEntity<Object> loginUser(@RequestBody User search) {
        return authService.loginUser(search);
    }
}