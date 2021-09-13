package authgrp.auth.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class User {

    @Id
    private String userId;
    private String password;
    private String firstname;
    private String lastname;
    private String token;

    public User(String userId, String password, String firstname, String lastname) {
        this.userId = userId;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User() {
    }

    // Getter-Methods
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstname;
    }

    //Setter-Methods

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //Functions
    public String generateAndSetToken() {
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklnmopqrstuvwxyz1234567890";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();
        while (token.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * ALPHABET.length());
            token.append(ALPHABET.charAt(index));
        }
        String returnToken = token.toString();
        setToken(returnToken);
        //setToken("postman");
        return this.token;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
