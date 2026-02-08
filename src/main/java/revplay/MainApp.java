package revplay;

import model.User;
import service.*;
import dao.SongDAO;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        MusicService music = new MusicService();
        PlaylistService playlist = new PlaylistService();

        while (true) {
            System.out.println("\n🎵 REVPLAY 🎵");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) {
                auth.register(sc);
            }
            else if (choice == 2) {

                User user = auth.login(sc);
                if (user == null) {
                    System.out.println("❌ Invalid login");
                    continue;
                }

                /* ================= LISTENER ================= */
                if (user.role.equals("LISTENER")) {

                    SongDAO songDAO = new SongDAO();

                    while (true) {
                        System.out.println("\nListener Menu");
                        System.out.println("1. View Songs");
                        System.out.println("2. Play Song");
                        System.out.println("3. Modify Playlists");
                        System.out.println("4. Logout");

                        int c = Integer.parseInt(sc.nextLine());

                        switch (c) {
                            case 1 -> songDAO.listAllSongs();

                            case 2 -> {
                                System.out.print("Enter Song ID: ");
                                int songId = Integer.parseInt(sc.nextLine());
                                music.play(songId, user.id, sc);
                            }

                            case 3 -> playlistMenu(sc, playlist, user.id);

                            case 4 -> {
                                System.out.println("👋 Logged out");
                                break;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }

                /* ================= ARTIST ================= */
                else if (user.role.equals("ARTIST")) {

                    SongDAO songDAO = new SongDAO();

                    while (true) {
                        System.out.println("\nArtist Menu");
                        System.out.println("1. Upload Song");
                        System.out.println("2. View My Songs");
                        System.out.println("3. Logout");

                        int c = Integer.parseInt(sc.nextLine());

                        if (c == 1) {
                            System.out.print("Song Title: ");
                            String title = sc.nextLine();

                            System.out.print("Duration (seconds): ");
                            int duration = Integer.parseInt(sc.nextLine());

                            songDAO.uploadSong(user.id, title, duration);
                            System.out.println("✅ Song uploaded successfully");
                        }
                        else if (c == 2) {
                            songDAO.listSongsByArtist(user.id);
                        }
                        else {
                            System.out.println("👋 Logged out");
                            break;
                        }
                    }
                }
            }
            else {
                break;
            }
        }
    }

    /* ================= PLAYLIST SUB-MENU ================= */
    private static void playlistMenu(
            Scanner sc,
            PlaylistService playlist,
            int userId) throws Exception {

        while (true) {
            System.out.println("\n📂 Playlist Menu");
            System.out.println("1. Create Playlist");
            System.out.println("2. View My Playlists");
            System.out.println("3. Add Song to Playlist");
            System.out.println("4. View Public Playlists");
            System.out.println("5. Delete Playlist");
            System.out.println("6. Back");

            int c = Integer.parseInt(sc.nextLine());

            switch (c) {
                case 1 -> playlist.create(userId, sc);

                case 2 -> playlist.viewMyPlaylists(userId);

                case 3 -> playlist.addSongToPlaylist(sc);

                case 4 -> playlist.viewPublicPlaylists();

                case 5 -> playlist.deletePlaylist(userId, sc);

                case 6 -> {
                    System.out.println("↩ Returning to Listener Menu");
                    return;
                }

                default -> System.out.println("Invalid option");
            }
        }
    }
}
