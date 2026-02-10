package dao;

import model.Song;
import util.DBConnection;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class SongDAO {

    // ================= GET SINGLE SONG =================
    public Song getSong(int id) throws Exception {

        String sql = "SELECT title, duration_seconds FROM songs WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Song s = new Song();
                s.id = id;
                s.title = rs.getString("title");
                s.duration = rs.getInt("duration_seconds");
                return s;
            }
            return null;
        }
    }

    // ================= RECORD PLAY =================
    public void recordPlay(int songId, int userId) throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            CallableStatement cs =
                    con.prepareCall("{call increment_play_count(?)}");
            cs.setInt(1, songId);
            cs.execute();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO listening_history(listener_id, song_id) VALUES (?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }
    }

    // ================= UPLOAD SONG =================
    public void uploadSong(
            int artistId,
            String title,
            int duration,
            Integer albumId) throws Exception {

        String validateAlbumSql =
                "SELECT 1 FROM albums WHERE id = ? AND artist_id = ?";

        String insertSql =
                "INSERT INTO songs(artist_id, album_id, title, duration_seconds) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // ---------- Validate album ownership (if album is provided) ----------
            if (albumId != null) {
                try (PreparedStatement check = con.prepareStatement(validateAlbumSql)) {
                    check.setInt(1, albumId);
                    check.setInt(2, artistId);

                    ResultSet rs = check.executeQuery();
                    if (!rs.next()) {
                        throw new IllegalArgumentException(
                                "Invalid album ID or album does not belong to you"
                        );
                    }
                }
            }

            // ---------- Insert song ----------
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {

                ps.setInt(1, artistId);

                if (albumId == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, albumId);
                }

                ps.setString(3, title);
                ps.setInt(4, duration);

                ps.executeUpdate();
            }
        }
    }



    // ================= LIST ALL SONGS =================
    public void listAllSongs() throws Exception {

        String sql = "SELECT id, title, duration_seconds FROM songs";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n🎶 Available Songs:");
            boolean found = false;

            while (rs.next()) {
                found = true;

                int id = rs.getInt("id");
                String title = rs.getString("title");
                int duration = rs.getInt("duration_seconds");

                int min = duration / 60;
                int sec = duration % 60;

                System.out.printf("%d - %s (%02d:%02d)%n",
                        id, title, min, sec);
            }

            if (!found) {
                System.out.println("No songs available.");
            }
        }
    }

    // ================= GET NEXT SONG =================
    public Integer getNextSongId(int currentSongId) throws Exception {

        String sql = """
            SELECT id FROM songs
            WHERE id > ?
            ORDER BY id
            FETCH FIRST 1 ROWS ONLY
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, currentSongId);
            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // ================= ADD TO FAVORITES (FIXED) =================
    public void addToFavorites(int userId, int songId) {

        String sql =
                "INSERT INTO favorites(listener_id, song_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();

            System.out.println("Added to favorites");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Song already in favorites");
        } catch (Exception e) {
            System.out.println("Could not add to favorites");
        }
    }

    // ================= VIEW FAVORITES =================
    public void viewFavorites(int userId) throws Exception {

        String sql = """
            SELECT s.id, s.title, s.duration_seconds
            FROM favorites f
            JOIN songs s ON f.song_id = s.id
            WHERE f.listener_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nYour Favorite Songs:");
            boolean found = false;

            while (rs.next()) {
                found = true;

                int duration = rs.getInt("duration_seconds");
                int min = duration / 60;
                int sec = duration % 60;

                System.out.printf("%d - %s (%02d:%02d)%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        min, sec);
            }

            if (!found) {
                System.out.println("No favorite songs yet.");
            }
        }
    }

    // ================= VIEW LISTENING HISTORY (FORMATTED) =================
    public void viewListeningHistory(int userId) throws Exception {

        String sql = """
            SELECT s.title, lh.played_at
            FROM listening_history lh
            JOIN songs s ON lh.song_id = s.id
            WHERE lh.listener_id = ?
            ORDER BY lh.played_at DESC
        """;

        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nListening History:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getString("title") +
                                " | Played at: " +
                                rs.getTimestamp("played_at")
                                        .toLocalDateTime()
                                        .format(fmt)
                );
            }

            if (!found) {
                System.out.println("No listening history found.");
            }
        }
    }

    public void listSongsByAlbum(int albumId) throws Exception {

        String sql =
                "SELECT id, title, duration_seconds FROM songs WHERE album_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n🎶 Songs in Album:");
            boolean found = false;

            while (rs.next()) {
                found = true;

                int d = rs.getInt("duration_seconds");
                System.out.printf(
                        "%d - %s (%02d:%02d)%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        d / 60, d % 60
                );
            }

            if (!found) {
                System.out.println("No songs found in this album.");
            }
        }
    }


    // ================= LIST SONGS BY ARTIST =================
    public void listSongsByArtist(int artistId) throws Exception {

        String sql =
                "SELECT id, title, duration_seconds FROM songs WHERE artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nYour Songs:");
            boolean found = false;

            while (rs.next()) {
                found = true;

                int duration = rs.getInt("duration_seconds");
                int min = duration / 60;
                int sec = duration % 60;

                System.out.printf("%d - %s (%02d:%02d)%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        min, sec);
            }

            if (!found) {
                System.out.println("No songs uploaded yet.");
            }
        }
    }
}
