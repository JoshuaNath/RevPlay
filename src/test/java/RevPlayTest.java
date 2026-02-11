import dao.*;
import model.User;
import service.AuthService;

import org.junit.jupiter.api.Test;

import dao.SongDAO;

import static org.junit.jupiter.api.Assertions.*;

public class RevPlayTest {

    private final UserDAO userDAO = new UserDAO();
    private final SongDAO songDAO = new SongDAO();
    private final PlaylistDAO playlistDAO = new PlaylistDAO();
    private final ArtistDAO artistDAO = new ArtistDAO();

    @Test
    void loginTest(){
        AuthService auth = new AuthService();
        assertNotNull(auth);
    }

    @Test
    void registerListenerShouldCreateUser() throws Exception {

        String username = "junit_listener_" + System.currentTimeMillis();

        userDAO.registerListener(
                username,
                "test123",
                username + "@email.com",
                "JUnit Listener",
                "Pet name?",
                "Tommy"
        );

        User user = userDAO.getUserByUsername(username);

        assertNotNull(user);
        assertEquals("LISTENER", user.role);
    }


    @Test
    void registerArtistShouldCreateArtistUser() throws Exception {

        String username = "junit_artist_" + System.currentTimeMillis();

        userDAO.registerArtist(
                username,
                "artist123",
                username + "@email.com",
                "JUnit Artist",
                "Birth city?",
                "Delhi",
                "JUnit Star",
                "Pop",
                true,
                "junit_insta",
                "junit_youtube"
        );

        User user = userDAO.getUserByUsername(username);

        assertNotNull(user);
        assertEquals("ARTIST", user.role);
    }



    @Test
    void getUserByUsernameShouldReturnUserIfExists() throws Exception {

        User user = userDAO.getUserByUsername("listener1");

        assertNotNull(user);
        assertEquals("listener1", user.username);
    }


    @Test
    void getSongByIdTest() throws Exception {
        SongDAO dao = new SongDAO();
        var song = dao.getSong(1);
        assertNotNull(song);
        assertNotNull(song.title);
    }


    @Test
    void getUserByUsernameShouldReturnNullIfNotExists() throws Exception {

        User user = userDAO.getUserByUsername("non_existing_user");

        assertNull(user);
    }


    @Test
    void getSongInvalidIdTest() throws Exception {
        SongDAO dao = new SongDAO();

        var song = dao.getSong(-999);

        assertNull(song);
    }


    @Test
    void securityQuestionShouldBeStoredCorrectly() throws Exception {

        String username = "security_user_" + System.currentTimeMillis();

        userDAO.registerListener(
                username,
                "pass123",
                username + "@email.com",
                "Security User",
                "Favorite color?",
                "Blue"
        );

        User user = userDAO.getUserByUsername(username);

        assertNotNull(user);
        assertEquals("Favorite color?", user.securityQuestion);
        assertEquals("Blue", user.securityAnswer);
    }



    @Test
    void passwordShouldMatchStoredValue() throws Exception {

        String username = "password_user_" + System.currentTimeMillis();

        userDAO.registerListener(
                username,
                "mypassword",
                username + "@email.com",
                "Password User",
                "Pet?",
                "Cat"
        );

        User user = userDAO.getUserByUsername(username);

        assertNotNull(user);
        assertEquals("mypassword", user.password);
    }



    @Test
    void listenerRoleShouldBeCorrect() throws Exception {

        User user = userDAO.getUserByUsername("listener1");

        if (user != null) {
            assertEquals("LISTENER", user.role);
        }
    }

    @Test
    void artistRoleShouldBeCorrect() throws Exception {

        User user = userDAO.getUserByUsername("artist1");

        if (user != null) {
            assertEquals("ARTIST", user.role);
        }
    }


    @Test
    void uploadSongShouldExecuteWithoutError() throws Exception {
        songDAO.uploadSong(2, "JUnit Song", 200, null);
        assertTrue(true);
    }

    @Test
    void getSongShouldReturnSongIfExists() throws Exception {
        assertNotNull(songDAO.getSong(1));
    }

    @Test
    void getSongShouldReturnNullIfInvalidId() throws Exception {
        assertNull(songDAO.getSong(9999));
    }

    @Test
    void searchSongsShouldExecute() throws Exception {
        songDAO.searchSongs("a");
        assertTrue(true);
    }


    @Test
    void createPlaylistShouldExecute() throws Exception {
        playlistDAO.createPlaylist(1, "JUnit Playlist");
        assertTrue(true);
    }

    @Test
    void addSongToPlaylistShouldExecute() throws Exception {
        playlistDAO.addSongToPlaylist(1, 1);
        assertTrue(true);
    }

    @Test
    void viewPublicPlaylistsShouldExecute() throws Exception {
        playlistDAO.viewPublicPlaylists();
        assertTrue(true);
    }



    @Test
    void addToFavoritesShouldExecute() throws Exception {
        songDAO.addToFavorites(1, 1);
        assertTrue(true);
    }

    @Test
    void viewFavoritesShouldExecute() throws Exception {
        songDAO.viewFavorites(1);
        assertTrue(true);
    }

    @Test
    void recordPlayShouldExecute() throws Exception {
        songDAO.recordPlay(1, 1);
        assertTrue(true);
    }

    @Test
    void viewListeningHistoryShouldExecute() throws Exception {
        songDAO.viewListeningHistory(1);
        assertTrue(true);
    }


    @Test
    void viewArtistProfileShouldExecute() throws Exception {
        artistDAO.viewProfile(2);
        assertTrue(true);
    }

    @Test
    void viewSongStatsShouldExecute() throws Exception {
        artistDAO.viewSongStats(2, 1);
        assertTrue(true);
    }

}






