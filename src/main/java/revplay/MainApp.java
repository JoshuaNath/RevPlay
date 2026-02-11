package revplay;

import model.User;
import service.*;
import dao.SongDAO;
import dao.ArtistDAO;
import dao.AlbumDAO;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        MusicService music = new MusicService();
        PlaylistService playlist = new PlaylistService();

        while (true) {
            System.out.println("\nREVPLAY");
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
                    System.out.println("Invalid login");
                    continue;
                }

                /* ================= LISTENER ================= */
                if (user.role.equals("LISTENER")) {

                    SongDAO songDAO = new SongDAO();
                    AlbumDAO albumDAO = new AlbumDAO();
                    ArtistDAO artistDAO = new ArtistDAO();

                    boolean loggedIn = true;

                    while (loggedIn) {
                        System.out.println("\nListener Menu");
                        System.out.println("1. View Songs");
                        System.out.println("2. View Albums");
                        System.out.println("3. View Songs in Album");
                        System.out.println("4. Play Song");
                        System.out.println("5. Search Songs");
                        System.out.println("6. Modify Playlists");
                        System.out.println("7. View Favorites");
                        System.out.println("8. View Listening History");
                        System.out.println("9. View Artist Profile");
                        System.out.println("10. Logout");

                        int c = Integer.parseInt(sc.nextLine());

                        switch (c) {

                            case 1 -> {
                                songDAO.listAllSongs();

                                System.out.print("\nEnter Song ID to play (0 to cancel): ");
                                int selected = Integer.parseInt(sc.nextLine());

                                if (selected != 0) {
                                    music.play(selected, user.id, sc);
                                }
                            }


                            case 2 -> albumDAO.listAllAlbums();

                            case 3 -> {
                                System.out.print("Enter Album ID: ");
                                int albumId = Integer.parseInt(sc.nextLine());
                                songDAO.listSongsByAlbum(albumId);
                            }

                            case 4 -> {
                                System.out.print("Enter Song ID: ");
                                int songId = Integer.parseInt(sc.nextLine());
                                music.play(songId, user.id, sc);
                            }

                            case 5 -> {
                                System.out.print("Enter search keyword: ");
                                String keyword = sc.nextLine();

                                songDAO.searchSongs(keyword);

                                System.out.print("\nEnter Song ID to play (0 to cancel): ");
                                int selected = Integer.parseInt(sc.nextLine());

                                if (selected != 0) {
                                    music.play(selected, user.id, sc);
                                }
                            }

                            case 6 -> playlistMenu(sc, playlist, user.id);

                            case 7 -> songDAO.viewFavorites(user.id);

                            case 8 -> songDAO.viewListeningHistory(user.id);

                            case 9 -> {
                                System.out.print("Enter Artist Username: ");
                                String username = sc.nextLine();

                                int artistId = artistDAO.getArtistIdByUsername(username);
                                if (artistId == -1) {
                                    System.out.println("Artist not found.");
                                } else {
                                    artistDAO.viewProfile(artistId);
                                }
                            }


                            case 10 -> {
                                System.out.println("Logged out");
                                loggedIn = false;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }


                /* ================= ARTIST ================= */
                else if (user.role.equals("ARTIST")) {

                    SongDAO songDAO = new SongDAO();
                    ArtistDAO artistDAO = new ArtistDAO();
                    AlbumDAO albumDAO = new AlbumDAO();

                    boolean loggedIn = true;

                    while (loggedIn) {
                        System.out.println("\nArtist Menu");
                        System.out.println("1. Create Album");
                        System.out.println("2. View My Albums");
                        System.out.println("3. Add Song to Album");
                        System.out.println("4. View My Songs");
                        System.out.println("5. View My Profile");
                        System.out.println("6. View Song Stats");
                        System.out.println("7. Logout");

                        int c = Integer.parseInt(sc.nextLine());

                        switch (c) {
                            case 1 -> {
                                System.out.print("Album Title: ");
                                String albumTitle = sc.nextLine();
                                albumDAO.createAlbum(user.id, albumTitle);
                            }


                            case 2 -> albumDAO.listAlbumsByArtist(user.id);


                            case 3 -> {
                                System.out.print("Song Title: ");
                                String title = sc.nextLine();

                                System.out.print("Duration (seconds): ");
                                int duration = Integer.parseInt(sc.nextLine());

                                System.out.println("\nSelect an Album:");
                                albumDAO.listAlbumsByArtist(user.id);

                                System.out.print("Album ID (0 for no album): ");
                                int albumId = Integer.parseInt(sc.nextLine());

                                songDAO.uploadSong(
                                        user.id,
                                        title,
                                        duration,
                                        albumId == 0 ? null : albumId
                                );

                                System.out.println("Song added to album");
                            }


                            case 4 -> songDAO.listSongsByArtist(user.id);

                            case 5 -> artistDAO.viewProfile(user.id);

                            case 6 -> {
                                System.out.print("Enter Song ID: ");
                                int songId = Integer.parseInt(sc.nextLine());
                                artistDAO.viewSongStats(user.id, songId);
                            }


                            case 7 -> {
                                System.out.println("Logged out");
                                loggedIn = false;
                            }

                            default -> System.out.println("Invalid option");
                        }
                    }
                }


            }
            else {
                System.out.println("Goodbye");
                break;
            }
        }

        sc.close();
    }

    /* ================= PLAYLIST SUB-MENU ================= */
    private static void playlistMenu(
            Scanner sc,
            PlaylistService playlist,
            int userId) throws Exception {

        while (true) {
            System.out.println("\nPlaylist Menu");
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
