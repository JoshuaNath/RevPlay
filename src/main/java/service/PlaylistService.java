package service;

import dao.PlaylistDAO;
import java.util.Scanner;

public class PlaylistService {
    private final PlaylistDAO dao = new PlaylistDAO();

    public void create(int userId, Scanner sc) throws Exception {
        System.out.print("Playlist Name: ");
        dao.createPlaylist(userId, sc.nextLine());
        System.out.println("📁 Playlist created");
    }
}
