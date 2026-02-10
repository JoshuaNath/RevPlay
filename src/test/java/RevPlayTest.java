import service.AuthService;
import service.PlaylistService;

import org.junit.jupiter.api.Test;

import dao.SongDAO;

import static org.junit.jupiter.api.Assertions.*;

public class RevPlayTest {

    @Test
    void loginTest(){
        AuthService auth = new AuthService();
        assertNotNull(auth);
    }
    
    @Test
    void getSongByIdTest() throws Exception {
        SongDAO dao = new SongDAO();
        var song = dao.getSong(1);
        assertNotNull(song);
        assertNotNull(song.title);
    }

    
    @Test
    void getSongInvalidIdTest() throws Exception {
        SongDAO dao = new SongDAO();

        var song = dao.getSong(-999);

        assertNull(song);
    }
    
    @Test
    void playlistServiceCreationTest(){
        PlaylistService playlistService = new PlaylistService();
        assertNotNull(playlistService);
    }


}