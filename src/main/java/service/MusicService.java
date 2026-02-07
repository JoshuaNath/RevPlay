package service;

import dao.SongDAO;
import model.Song;

import java.util.Scanner;

public class MusicService {
    private final SongDAO dao = new SongDAO();

    public void play(int songId, int userId, Scanner sc) throws Exception {
        Song s = dao.getSong(songId);

        if (s == null) {
            System.out.println("❌ Song not found. Please select a valid song ID.");
            return;
        }

        System.out.println("▶ Now Playing: " + s.title);

        boolean running = true;
        while (running) {
            System.out.println("1. Pause  2. Skip  3. Repeat  4. Stop");
            int c = Integer.parseInt(sc.nextLine());

            switch (c) {
                case 1 -> System.out.println("⏸ Paused");
                case 2 -> running = false;
                case 3 -> System.out.println("🔁 Replaying");
                case 4 -> running = false;
            }
        }

        dao.recordPlay(songId, userId);
    }

}
