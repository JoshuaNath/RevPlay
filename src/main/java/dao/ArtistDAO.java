package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArtistDAO {

    // ================= VIEW ARTIST PROFILE =================
    public void viewProfile(int artistId) throws Exception {

        String profileSql = """
            SELECT artist_name, genre
            FROM artists
            WHERE user_id = ?
        """;

        String songsSql = """
            SELECT title, duration_seconds
            FROM songs
            WHERE artist_id = ?
        """;

        try (Connection con = DBConnection.getConnection()) {

            // ---- Artist basic info ----
            PreparedStatement psProfile = con.prepareStatement(profileSql);
            psProfile.setInt(1, artistId);
            ResultSet rsProfile = psProfile.executeQuery();

            if (!rsProfile.next()) {
                System.out.println("Artist profile not found.");
                return;
            }

            String name = rsProfile.getString("artist_name");
            String genre = rsProfile.getString("genre");

            System.out.println("\nArtist Profile");
            System.out.println("Name  : " + name);
            System.out.println("Genre : " + genre);

            // ---- Songs list ----
            PreparedStatement psSongs = con.prepareStatement(songsSql);
            psSongs.setInt(1, artistId);
            ResultSet rsSongs = psSongs.executeQuery();

            System.out.println("\nSongs Uploaded:");
            boolean found = false;

            int totalSongs = 0;
            int totalDuration = 0;
            int index = 1;

            while (rsSongs.next()) {
                found = true;

                String title = rsSongs.getString("title");
                int duration = rsSongs.getInt("duration_seconds");

                int min = duration / 60;
                int sec = duration % 60;

                System.out.printf(
                        "%d. %s (%02d:%02d)%n",
                        index++, title, min, sec
                );

                totalSongs++;
                totalDuration += duration;
            }

            if (!found) {
                System.out.println("No songs uploaded yet.");
            }

            int totalMin = totalDuration / 60;
            int totalSec = totalDuration % 60;

            System.out.println("\nTotal Songs : " + totalSongs);
            System.out.println("Total Time  : " + totalMin + " min " + totalSec + " sec");
        }
    }
}
