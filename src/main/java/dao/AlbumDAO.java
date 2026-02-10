package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AlbumDAO {

    // ================= CREATE ALBUM =================
    public void createAlbum(int artistId, String title) throws Exception {

        String sql =
                "INSERT INTO albums(artist_id, title, release_date) VALUES (?, ?, SYSDATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ps.setString(2, title);
            ps.executeUpdate();

            System.out.println("Album created successfully");
        }
    }

    public void listAlbumsByArtist(int artistId) throws Exception {

        String sql =
                "SELECT id, title FROM albums WHERE artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nYour Albums:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("id") + " - " + rs.getString("title")
                );
            }

            if (!found) {
                System.out.println("No albums created yet.");
            }
        }
    }


    // ================= LIST ALL ALBUMS (LISTENER) =================
    public void listAllAlbums() throws Exception {

        String sql = """
            SELECT a.id, a.title, ar.artist_name, a.release_date
            FROM albums a
            JOIN artists ar ON a.artist_id = ar.user_id
            ORDER BY a.release_date DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nAvailable Albums:");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("id") + " - " +
                                rs.getString("title") +
                                " | Artist: " + rs.getString("artist_name") +
                                " | Released: " + rs.getDate("release_date")
                );
            }

            if (!found) {
                System.out.println("No albums available.");
            }
        }
    }
}
