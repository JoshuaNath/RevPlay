package service;

import dao.PlaylistDAO;
import java.util.Scanner;

public class PlaylistService {

    private final PlaylistDAO dao = new PlaylistDAO();

    public void create(int userId, Scanner sc) throws Exception {
        System.out.print("Playlist Name: ");
        dao.createPlaylist(userId, sc.nextLine());
        System.out.println("Playlist created");
    }

    public void viewMyPlaylists(int userId) throws Exception {
        dao.viewMyPlaylists(userId);
    }

    public void addSongToPlaylist(Scanner sc) throws Exception {
        System.out.print("Playlist ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Song ID: ");
        int sid = Integer.parseInt(sc.nextLine());

        dao.addSongToPlaylist(pid, sid);
        System.out.println("Song added to playlist");
    }

    public void viewPublicPlaylists() throws Exception {
        dao.viewPublicPlaylists();
    }

    public void deletePlaylist(int userId, Scanner sc) throws Exception {
        System.out.print("Playlist ID to delete: ");
        int pid = Integer.parseInt(sc.nextLine());
        dao.deletePlaylist(pid, userId);
    }
}
