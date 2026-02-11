package service;

import dao.SongDAO;
import model.Song;

import java.util.Scanner;

public class MusicService {

    private final SongDAO dao = new SongDAO();

    public void play(int songId, int userId, Scanner sc) throws Exception {

        int currentSongId = songId;

        while (true) {

            Song s = dao.getSong(currentSongId);
            if (s == null) {
                System.out.println("Song not found.");
                return;
            }

            System.out.println("\n▶ Now Playing: " + s.title);
            System.out.println("Press 1 anytime to Pause");

            int totalSeconds = s.duration;
            int elapsedSeconds = 0;

            while (elapsedSeconds < totalSeconds) {

                printProgress(elapsedSeconds, totalSeconds);
                Thread.sleep(1000);
                elapsedSeconds++;

                // Pause detection
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

                                // Record current song play
                                dao.recordPlay(currentSongId, userId);

                                // Get next song details immediately
                                Song nextSong = dao.getSong(nextSongId);

                                System.out.println("\n⏭ Skipped");
                                System.out.println("🎵 Now Playing: " + nextSong.title);

                                currentSongId = nextSongId;
                                elapsedSeconds = 0;

                                break;
                            }



                            case 3 -> {
                                System.out.println("Replaying");
                                elapsedSeconds = 0;
                            }

                            case 4 -> dao.addToFavorites(userId, currentSongId);

                            case 5 -> {
                                dao.recordPlay(currentSongId, userId);
                                System.out.println("Stopped");
                                return;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }
            }

            // Song finished naturally
            dao.recordPlay(currentSongId, userId);
            System.out.println("\nSong finished");

            Integer nextSongId = dao.getNextSongId(currentSongId);
            if (nextSongId == null) {
                return;
            }

            currentSongId = nextSongId;
        }
    }

    // ================= PAUSE MENU =================
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

    // ================= PROGRESS BAR =================
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
