package authgrp.auth;

import authgrp.auth.controller.UserController;
import authgrp.auth.model.User;
import authgrp.auth.repo.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthApplicationTests {
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userRepository)).build();
    }

    @Test
    @Order(1)
    public void loginTest() throws Exception {
        String userToFind = "{\"userId\":\"eschuler\","
                + "\"password\":\"pass1234\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToFind))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization")).andReturn();

        String token = result.getResponse().getContentAsString();

        User u = userRepository.findByUserId("eschuler");
        assertEquals("eschuler", u.getUserId());
        assertEquals("Elena", u.getFirstName());
        assertEquals("Schuler", u.getLastName());
        assertEquals(u.getToken(), token);
    }

    @Test
    //@Order(2)
    public void loginTestFail() throws Exception {
        //wrong pw
        userRepository.findAll().forEach(x -> System.out.println(x.toString()));
        String userToFind = "{\"userId\":\"eschuler\","
                + "\"password\":\"pass1234-wrong\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToFind))
                .andExpect(status().isUnauthorized());

        //wrong username
        userRepository.findAll().forEach(x -> System.out.println(x.toString()));
        userToFind = "{\"userId\":\"eschuler-wrong\","
                + "\"password\":\"pass1234\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToFind))
                .andExpect(status().isUnauthorized());

        //wrong combination
        userRepository.findAll().forEach(x -> System.out.println(x.toString()));
        userToFind = "{\"userId\":\"eneuman\","
                + "\"password\":\"pass1234\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userToFind))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void checkUserTest() throws Exception {
        //need test #1 so that a token is generated already!
        User userToCheck = userRepository.findByUserId("eschuler");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", userToCheck.getToken());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/auth")
                .headers(headers))
                .andExpect(status().isOk())
                .andReturn();

        String receivedUserStr = result.getResponse().getContentAsString();
        assertEquals(receivedUserStr, userToCheck.getUserId());
    }

    @Test
    void checkUserTestFail() throws Exception {
        //need test #1 so that a token is generated already!
        User userToCheck = userRepository.findByUserId("eschuler");
        HttpHeaders headers = new HttpHeaders();

        mockMvc.perform(MockMvcRequestBuilders.get("/auth"))
                .andExpect(status().isBadRequest());

        headers.set("Authorization", "wrongToken");
        mockMvc.perform(MockMvcRequestBuilders.get("/auth")
                .headers(headers))
                .andExpect(status().isUnauthorized());

        headers.set("Authorization", userToCheck.getToken() + "wrong");
        mockMvc.perform(MockMvcRequestBuilders.get("/auth")
                .headers(headers))
                .andExpect(status().isUnauthorized());

        //make sure it will work with correct header
        headers.set("Authorization", userToCheck.getToken());
        mockMvc.perform(MockMvcRequestBuilders.get("/auth")
                .headers(headers))
                .andExpect(status().isOk());
    }

    @Test
    void userBasicTest() {
        User u = new User("userId", "password", "firstname", "lastname");
        assertEquals(u.getUserId(), "userId");
        assertEquals(u.getPassword(), "password");
        assertEquals(u.getFirstName(), "firstname");
        assertEquals(u.getLastName(), "lastname");
        u.setFirstName("fistnameNew");
        u.setLastName("lastnameNew");
        assertEquals(u.getFirstName(), "fistnameNew");
        assertEquals(u.getLastName(), "lastnameNew");
    }
}
