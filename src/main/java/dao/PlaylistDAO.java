package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlaylistDAO {

    // 1. Create playlist
    public void createPlaylist(int userId, String name) throws Exception {
        String sql = "INSERT INTO playlists(listener_id, name) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }

    // 2. View my playlists
    public void viewMyPlaylists(int userId) throws Exception {
        String sql = "SELECT id, name FROM playlists WHERE listener_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n📂 My Playlists:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }

            if (!found) {
                System.out.println("No playlists found.");
            }
        }
    }

    //  Song is already in playlist
    public boolean isSongAlreadyInPlaylist(int playlistId, int songId) throws Exception {
        String sql = """
            SELECT COUNT(*)
            FROM playlist_songs
            WHERE playlist_id = ? AND song_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    // 3. Add song to playlist
    public void addSongToPlaylist(int playlistId, int songId) throws Exception {

        if (isSongAlreadyInPlaylist(playlistId, songId)) {
            System.out.println("⚠️ Song already exists in this playlist.");
            return;
        }

        String sql = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.executeUpdate();
            System.out.println("🎵 Song added to playlist");
        }
    }

    // 4. View public playlists
    public void viewPublicPlaylists() throws Exception {
        String sql = "SELECT id, name FROM playlists WHERE is_public = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            System.out.println("\n🌍 Public Playlists:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }

            if (!found) {
                System.out.println("No public playlists available.");
            }
        }
    }

    // 5. Delete playlist
    public void deletePlaylist(int playlistId, int userId) throws Exception {
        String sql = "DELETE FROM playlists WHERE id = ? AND listener_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("Playlist not found or not owned by you.");
            } else {
                System.out.println("Playlist deleted.");
            }
        }
    }
}
