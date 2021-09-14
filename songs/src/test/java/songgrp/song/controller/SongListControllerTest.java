package songgrp.song.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import songgrp.song.model.SongList;
import songgrp.song.repo.SongListRepository;
import songgrp.song.repo.SongRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author github.com/butburg (EW) on Sep 2021
 */
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SongListControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private SongRepository songRepo;
    @Autowired
    private SongListRepository playlistRepo;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SongListController(playlistRepo)).build();
    }


    @Test
    void getSongListTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Iterable<SongList> playlist = playlistRepo.findAllById(List.of(2, 3, 4));
        String wantedPlaylist = mapper.writeValueAsString(playlist);
        System.out.println("TEST" + wantedPlaylist);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "userToCheck.getToken()");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/songLists")
                .headers(headers))
                .andExpect(status().isOk())
                .andReturn();
        String playlistRespond = result.getResponse().getContentAsString();

        assertEquals(wantedPlaylist, playlistRespond);


    }

    @Test
    void getAllSongListsTest() {
    }

    @Test
    void changeSongListTest() {
    }

    @Test
    void deleteSongListTest() {
    }

    @Test
    void deleteWrongPathTest() {
    }
}