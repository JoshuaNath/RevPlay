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
                System.out.println("❌ Song not found.");
                return;
            }

            System.out.println("▶ Now Playing: " + s.title);

            boolean playing = true;
            while (playing) {
                System.out.println("1. Pause  2. Skip  3. Repeat  4. Stop");
                int c = Integer.parseInt(sc.nextLine());

                switch (c) {
                    case 1 -> System.out.println("⏸ Paused");

                    case 2 -> {
                        Integer nextSongId = dao.getNextSongId(currentSongId);
                        if (nextSongId == null) {
                            System.out.println("⛔ No next song available");
                            return;
                        }
                        currentSongId = nextSongId;
                        playing = false;   // exit inner loop, play next
                    }

                    case 3 -> System.out.println("🔁 Replaying");

                    case 4 -> {
                        dao.recordPlay(currentSongId, userId);
                        return;
                    }
                }
            }

            dao.recordPlay(currentSongId, userId);
        }
    }


}
