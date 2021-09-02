package controller;


import dao.IUserDAO;
import model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class UserControllerDI {

    private IUserDAO userDAO;

    public UserControllerDI(IUserDAO iDAO) {
        this.userDAO = iDAO;
    }

    @PostMapping
    public ResponseEntity<Object> loginUser(@RequestBody User search) {

        String searchedUserId = search.getUserId();
        String searchedUserPw = search.getPassword();
        User user = userDAO.findUser(searchedUserId);

        if (user == null || user.getUserId() == null ||
                user.getPassword() == null) {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getUserId().equals(searchedUserId) && user.getPassword().equals(searchedUserPw)) {
            String token = user.generateToken();
            HttpHeaders authHeader = new HttpHeaders();
            authHeader.add("Authorization", user.getToken());
            userDAO.updateToken(user);
            return new ResponseEntity<Object>(token, authHeader, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }


    }

}
