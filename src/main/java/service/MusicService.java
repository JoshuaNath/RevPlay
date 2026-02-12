package service;

import dao.SongDAO;
import model.Song;
import util.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class MusicService {

    private final SongDAO dao = new SongDAO();

    // SONG PLAY

    public void play(int songId, int userId, Scanner sc) throws Exception {

        int currentSongId = songId;

        while (true) {

            Song s = dao.getSong(currentSongId);
            if (s == null) {
                System.out.println("Song not found.");
                return;
            }

            System.out.println("\n🎵 Now Playing: " + s.title);
            System.out.println("Press 1 anytime to Pause");

            int totalSeconds = s.duration;
            int elapsedSeconds = 0;

            while (elapsedSeconds < totalSeconds) {

                printProgress(elapsedSeconds, totalSeconds);
                Thread.sleep(1000);
                elapsedSeconds++;

                if (System.in.available() > 0) {
                    String input = sc.nextLine();

                    if ("1".equals(input)) {

                        System.out.println("\n⏸ Paused");
                        int action = pausedMenu(sc);

                        switch (action) {

                            case 1 -> System.out.println("▶ Resumed");

                            case 2 -> {
                                Integer nextSongId = dao.getNextSongId(currentSongId);

                                if (nextSongId == null) {
                                    System.out.println("No next song available");
                                    dao.recordPlay(currentSongId, userId);
                                    return;
                                }

                                dao.recordPlay(currentSongId, userId);

                                Song nextSong = dao.getSong(nextSongId);
                                System.out.println("\n⏭ Skipped");
                                System.out.println("🎵 Now Playing: " + nextSong.title);

                                currentSongId = nextSongId;
                                elapsedSeconds = 0;
                                break;
                            }

                            case 3 -> {
                                System.out.println("🔁 Replaying");
                                elapsedSeconds = 0;
                            }

                            case 4 -> dao.addToFavorites(userId, currentSongId);

                            case 5 -> {
                                dao.recordPlay(currentSongId, userId);
                                System.out.println("⏹ Stopped");
                                return;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }
            }

            dao.recordPlay(currentSongId, userId);
            System.out.println("\n🎵 Song finished");

            Integer nextSongId = dao.getNextSongId(currentSongId);
            if (nextSongId == null) {
                return;
            }

            Song nextSong = dao.getSong(nextSongId);
            System.out.println("\n▶ Auto Playing Next: " + nextSong.title);

            currentSongId = nextSongId;
        }
    }


    // PODCAST PLAY

    public void playPodcastEpisode(int episodeId, int userId, Scanner sc) throws Exception {

        try (Connection con = DBConnection.getConnection()) {

            String sql = """
                SELECT title, duration_seconds
                FROM podcast_episodes
                WHERE id = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, episodeId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Episode not found.");
                return;
            }

            String title = rs.getString("title");
            int totalSeconds = rs.getInt("duration_seconds");

            System.out.println("\n🎙 Now Playing Podcast: " + title);
            System.out.println("Press 1 anytime to Pause");

            int elapsed = 0;

            while (elapsed < totalSeconds) {

                printProgress(elapsed, totalSeconds);
                Thread.sleep(1000);
                elapsed++;

                if (System.in.available() > 0) {
                    String input = sc.nextLine();

                    if ("1".equals(input)) {

                        System.out.println("\n⏸ Paused");

                        System.out.println("""
                                1. Resume
                                2. Repeat
                                3. Stop
                                """);

                        System.out.print("Choose option: ");
                        int action = Integer.parseInt(sc.nextLine());

                        switch (action) {

                            case 1 -> System.out.println("▶ Resumed");

                            case 2 -> {
                                System.out.println("🔁 Replaying Episode");
                                elapsed = 0;
                            }

                            case 3 -> {
                                recordPodcastPlay(con, episodeId, userId);
                                System.out.println("⏹ Stopped");
                                return;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }
            }

            System.out.println("\n🎙 Episode finished");
            recordPodcastPlay(con, episodeId, userId);
        }
    }


    //  PODCAST PLAY RECORD


    private void recordPodcastPlay(Connection con, int episodeId, int userId) throws Exception {

        // Increment play count
        PreparedStatement update = con.prepareStatement(
                "UPDATE podcast_episodes SET play_count = play_count + 1 WHERE id = ?"
        );
        update.setInt(1, episodeId);
        update.executeUpdate();

        // Insert into podcast history
        PreparedStatement history = con.prepareStatement(
                "INSERT INTO podcast_history(listener_id, episode_id) VALUES (?, ?)"
        );
        history.setInt(1, userId);
        history.setInt(2, episodeId);
        history.executeUpdate();
    }


    // PAUSE MENU

    private int pausedMenu(Scanner sc) {
        System.out.println("""
                1. Resume
                2. Skip
                3. Repeat
                4. Add to Favorites
                5. Stop
                """);
        System.out.print("Choose option: ");
        return Integer.parseInt(sc.nextLine());
    }


    // PROGRESS BAR

    private void printProgress(int elapsed, int total) {

        int barLength = 20;
        int filled = (int) (((double) elapsed / total) * barLength);

        String bar =
                "█".repeat(filled) +
                        "░".repeat(barLength - filled);

        System.out.printf(
                "\r[%s] %s / %s",
                bar,
                formatTime(elapsed),
                formatTime(total)
        );
    }

    private String formatTime(int seconds) {
        int mm = seconds / 60;
        int ss = seconds % 60;
        return String.format("%02d:%02d", mm, ss);
    }
}
