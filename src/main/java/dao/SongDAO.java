package dao;

import model.Song;
import util.DBConnection;

import java.sql.*;

public class SongDAO {

    public Song getSong(int id) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT title,duration_seconds FROM songs WHERE id=?");
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Song s = new Song();
            s.id = id;
            s.title = rs.getString(1);
            s.duration = rs.getInt(2);
            return s;
        }
        return null;
    }

    public void recordPlay(int songId, int userId) throws Exception {
        Connection con = DBConnection.getConnection();

        CallableStatement cs =
                con.prepareCall("{call increment_play_count(?)}");
        cs.setInt(1, songId);
        cs.execute();

        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO listening_history(listener_id,song_id) VALUES (?,?)");
        ps.setInt(1, userId);
        ps.setInt(2, songId);
        ps.executeUpdate();

        con.close();
    }

    public void uploadSong(int artistId, String title, int duration) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO songs(artist_id,title,duration_seconds) VALUES (?,?,?)");
        ps.setInt(1, artistId);
        ps.setString(2, title);
        ps.setInt(3, duration);
        ps.executeUpdate();
        con.close();
    }
    
    
    public void listAllSongs() throws Exception {
        String sql = "SELECT id, title FROM songs";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n🎶 Available Songs:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("id") + " - " + rs.getString("title")
                );
            }

            if (!found) {
                System.out.println("No songs available.");
            }
        }
    }
    
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

            if (rs.next()) {
                return rs.getInt("id");
            }
            return null;
        }
    }
    
    
    public void listSongsByArtist(int artistId) throws Exception {
        String sql = "SELECT id, title FROM songs WHERE artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n🎤 Your Songs:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("id") + " - " + rs.getString("title")
                );
            }

            if (!found) {
                System.out.println("No songs uploaded yet.");
            }
        }
    }



}
