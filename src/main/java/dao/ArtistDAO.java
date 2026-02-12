package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArtistDAO {

    // ================= VIEW ARTIST PROFILE =================
    public void viewProfile(int artistId) throws Exception {

        String profileSql = """
            SELECT artist_name, genre,
                   disclose_social,
                   instagram_id,
                   youtube_channel
            FROM artists
            WHERE user_id = ?
        """;

        String songsSql = """
            SELECT title, duration_seconds
            FROM songs
            WHERE artist_id = ?
        """;

        try (Connection con = DBConnection.getConnection()) {

            // ===== BASIC INFO =====
            PreparedStatement psProfile = con.prepareStatement(profileSql);
            psProfile.setInt(1, artistId);
            ResultSet rsProfile = psProfile.executeQuery();

            if (!rsProfile.next()) {
                System.out.println("Artist profile not found.");
                return;
            }

            String name = rsProfile.getString("artist_name");
            String genre = rsProfile.getString("genre");
            int disclose = rsProfile.getInt("disclose_social");

            System.out.println("\nArtist Profile");
            System.out.println("Name  : " + name);
            System.out.println("Genre : " + genre);

            // ===== SOCIAL MEDIA =====
            if (disclose == 1) {
                System.out.println("Instagram : " + rsProfile.getString("instagram_id"));
                System.out.println("YouTube   : " + rsProfile.getString("youtube_channel"));
            } else {
                System.out.println("Social Media : The artist does not want to disclose their social media accounts");
            }

            // ===== SONGS =====
            PreparedStatement psSongs = con.prepareStatement(songsSql);
            psSongs.setInt(1, artistId);
            ResultSet rsSongs = psSongs.executeQuery();

            System.out.println("\n🎶 Songs Uploaded:");
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

    public void viewSongStats(int artistId, int songId) throws Exception {

        String validateSql =
                "SELECT title, play_count FROM songs WHERE id = ? AND artist_id = ?";

        String topListenerSql = """
        SELECT u.username, COUNT(*) as play_times
        FROM listening_history lh
        JOIN users u ON lh.listener_id = u.id
        WHERE lh.song_id = ?
        GROUP BY u.username
        ORDER BY play_times DESC
        FETCH FIRST 1 ROWS ONLY
    """;

        String topDaySql = """
        SELECT TO_CHAR(played_at, 'YYYY-MM-DD') as play_day,
               COUNT(*) as play_count
        FROM listening_history
        WHERE song_id = ?
        GROUP BY TO_CHAR(played_at, 'YYYY-MM-DD')
        ORDER BY play_count DESC
        FETCH FIRST 1 ROWS ONLY
    """;

        String favoritesSql =
                "SELECT COUNT(*) FROM favorites WHERE song_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            // ===== Validate song belongs to artist =====
            PreparedStatement psValidate = con.prepareStatement(validateSql);
            psValidate.setInt(1, songId);
            psValidate.setInt(2, artistId);
            ResultSet rsValidate = psValidate.executeQuery();

            if (!rsValidate.next()) {
                System.out.println("Song not found or does not belong to you.");
                return;
            }

            String title = rsValidate.getString("title");
            int totalPlays = rsValidate.getInt("play_count");

            System.out.println("\nSong Statistics");
            System.out.println("Song Name     : " + title);
            System.out.println("Total Plays   : " + totalPlays);

            // ===== Top Listener =====
            PreparedStatement psListener = con.prepareStatement(topListenerSql);
            psListener.setInt(1, songId);
            ResultSet rsListener = psListener.executeQuery();

            if (rsListener.next()) {
                System.out.println("Top Listener  : " +
                        rsListener.getString("username") +
                        " (" + rsListener.getInt("play_times") + " plays)");
            } else {
                System.out.println("Top Listener  : No plays yet");
            }

            // ===== Top Day =====
            PreparedStatement psDay = con.prepareStatement(topDaySql);
            psDay.setInt(1, songId);
            ResultSet rsDay = psDay.executeQuery();

            if (rsDay.next()) {
                System.out.println("Most Played On: " +
                        rsDay.getString("play_day") +
                        " (" + rsDay.getInt("play_count") + " plays)");
            } else {
                System.out.println("Most Played On: No plays yet");
            }

            // ===== Favorites Count =====
            PreparedStatement psFav = con.prepareStatement(favoritesSql);
            psFav.setInt(1, songId);
            ResultSet rsFav = psFav.executeQuery();

            if (rsFav.next()) {
                System.out.println("Favorites     : " + rsFav.getInt(1));
            }

            System.out.println();
        }
    }


    // ================= FIND ARTIST BY USERNAME =================
    public int getArtistIdByUsername(String username) throws Exception {

        String sql = """
            SELECT u.id
            FROM users u
            WHERE u.username = ?
              AND u.user_type = 'ARTIST'
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        }
    }
}
