import controller.SongControllerDI;
import controller.SongListControllerDI;
import controller.UserControllerDI;
import dao.*;
import model.SongList;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SongListEndpointTest {

    private MockMvc songMockMvc;
    private MockMvc userMockMvc;
    private MockMvc songListMockMvc;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Songs-TEST-PU");
    private HttpHeaders httpHeaders;
    private IUserDAO userDAO = new DBUserDAO(emf);
    private ISongDAO songDAO = new DBSongDAO(emf);
    private ISongListDAO songListDAO = new DBSongListDAO(emf);


    @BeforeEach
    public void setMockMvc() throws Exception {
        songMockMvc = MockMvcBuilders.standaloneSetup(new SongControllerDI(songDAO, userDAO)).build();
        songListMockMvc = MockMvcBuilders.standaloneSetup(new SongListControllerDI(songListDAO, userDAO, songDAO)).build();
        userMockMvc = MockMvcBuilders.standaloneSetup(new UserControllerDI(userDAO)).build();

        String userAuth = "{ \"userId\":\"psaal\"," +
                "\"password\":\"pw12\"}";

        userMockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userAuth));
        httpHeaders = new HttpHeaders();

        String userToken = userDAO.findUser("psaal").getToken();
        httpHeaders.add("Authorization", userToken);
    }

    @Test
    @Order(1)
    public void getAllSongsTest() throws Exception {
        songMockMvc.perform(MockMvcRequestBuilders.get("/songs").headers(httpHeaders));
    }

    @Test
    @Order(2)
    public void addASongTest() throws Exception {
        String songToSave = "{\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}";
        songMockMvc.perform(MockMvcRequestBuilders.post("/songs")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songToSave))
                .andExpect(status().isCreated());
        assertEquals(3, songDAO.findAllSongs().size());
    }

    @Test
    @Order(3)
    public void updateSongTest() throws Exception{
        String songToUpdate = "{\"id\":1,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}";

        String oldTitle = songDAO.findSong(1).getTitle();
        songMockMvc.perform(MockMvcRequestBuilders.put("/songs/1")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songToUpdate)).andExpect(status().isNoContent());

        assertNotSame(oldTitle, songDAO.findSong(1).getTitle());
    }

    @Test
    @Order(4)
    public void getOwnSongListsTest() throws Exception {

        songListMockMvc.perform(MockMvcRequestBuilders.get("/songLists/")
                .headers(httpHeaders))
                .andExpect(status().isOk());

        assertEquals(2, songListDAO.findAllSongListsOf("psaal").size());

    }

    @Test
    @Order(5)
    public void getOtherPublicSongListsTest() throws Exception {

        songListMockMvc.perform(MockMvcRequestBuilders.get("/songLists?userId=ewiese")
                .headers(httpHeaders))
                .andExpect(status().isOk());
        int amountOfPublicList = 0;
        for (SongList s: songListDAO.findAllSongListsOf("ewiese")) {
            if (!s.isPrivate()){
                amountOfPublicList++;
            }
        }

        assertEquals(1, amountOfPublicList);

    }

    @Test
    @Order(6)
    public void addSongListTest() throws Exception {
        String songListToSave = "{"
                + "\"isPrivate\":true,"
                + "\"name\":\"test1\", "
                + "\"songList\":["
                + "{\"id\":3,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}]}";

        songListMockMvc.perform(MockMvcRequestBuilders.post("/songLists/")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songListToSave))
                .andExpect(status().isCreated());

        assertEquals(3, songListDAO.findAllSongListsOf("psaal").size());
    }

    @Test
    @Order(7)
    public void deleteSongListTest() throws Exception {
        songListMockMvc.perform(MockMvcRequestBuilders.delete("/songLists/2")
                .headers(httpHeaders))
                .andExpect(status().isNoContent());
        assertEquals(2, songListDAO.findAllSongListsOf("psaal").size());
    }

    @Test
    @Order(8)
    public void deleteSongListOtherUserTest() throws Exception {
        songListMockMvc.perform(MockMvcRequestBuilders.delete("/songLists/3")
                .headers(httpHeaders))
                .andExpect(status().isForbidden());
        assertEquals(2, songListDAO.findAllSongListsOf("ewiese").size());
    }

    @Test
    @Order(9)
    public void getOwnSongListWithParamTest() throws Exception {
        songListMockMvc.perform(MockMvcRequestBuilders.get("/songLists?userId=psaal")
                .headers(httpHeaders))
                .andExpect(status().isOk());
        assertEquals(2, songListDAO.findAllSongListsOf("psaal").size());
    }

    @Test
    @Order(10)
    public void wrongAuthTest() throws Exception {
        String songToSave = "{\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}";
        HttpHeaders httpHeadersTest = httpHeaders;
        httpHeadersTest.set("Authorization","shouldNotWork");
        songMockMvc.perform(MockMvcRequestBuilders.post("/songs/")
                .headers(httpHeadersTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songToSave))
                .andExpect(status().isUnauthorized());
        assertEquals(3, songDAO.findAllSongs().size());
    }

    @Test
    @Order(11)
    public void wrongTitleSongTest() throws Exception{
        String songToSave = "{\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"titleSpelledWrong\":\"Wrecking Ball\"}";
        songMockMvc.perform(MockMvcRequestBuilders.post("/songs/")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songToSave))
                .andExpect(status().isBadRequest());
        assertEquals(3, songDAO.findAllSongs().size());
    }

    @Test
    @Order(12)
    public void noTitleSongTest() throws Exception{
        String songToSave = "{\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "}";
        songMockMvc.perform(MockMvcRequestBuilders.post("/songs/")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songToSave))
                .andExpect(status().isBadRequest());
        assertEquals(3, songDAO.findAllSongs().size());
    }

    @Test
    @Order(13)
    public void addMoreThenOneSongListTest() throws Exception {
        String songListToSave = "{"
                + "\"isPrivate\":true,"
                + "\"name\":\"test1\", "
                + "\"songList\":["
                + "{\"id\":3,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"},"
                + "{\"id\":1,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}"
                + "]}";

        songListMockMvc.perform(MockMvcRequestBuilders.post("/songLists/")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songListToSave))
                .andExpect(status().isCreated());

        assertEquals(3, songListDAO.findAllSongListsOf("psaal").size());
    }

    @Test
    @Order(14)
    public void addSongListWithNotExsistingSongTest() throws Exception {
        String songListToSave = "{"
                + "\"isPrivate\":true,"
                + "\"name\":\"test1\", "
                + "\"songList\":["
                + "{\"id\":3,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Breaking Ball\"},"
                + "{\"id\":1,"
                + "\"artist\":\"MILEY CYRUS\","
                + "\"label\":\"RCA\", "
                + "\"released\":2013,"
                + "\"title\":\"Wrecking Ball\"}"
                + "]}";

        songListMockMvc.perform(MockMvcRequestBuilders.post("/songLists/")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(songListToSave))
                .andExpect(status().isBadRequest());

        assertEquals(3, songListDAO.findAllSongListsOf("psaal").size());
    }

    @Test
    @Order(15)
    public void getNoSongListWithParamTest() throws Exception {
        songListMockMvc.perform(MockMvcRequestBuilders.get("/songLists?userId=mschuler")
                .headers(httpHeaders))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(16)
    public void wrongUserDataAuthTest() throws Exception {
        String userAuth = "{ \"userId\":\"psaal\"," +
                "\"password\":\"12\"}";
        userMockMvc.perform(MockMvcRequestBuilders.post("/auth")).andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    public void getPrivateSongListFromOtherUserTest() throws Exception {
        songListMockMvc.perform(MockMvcRequestBuilders.get("/songLists/3")
                .headers(httpHeaders))
                .andExpect(status().isForbidden());
    }

}