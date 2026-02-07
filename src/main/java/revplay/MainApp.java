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

                if (user.role.equals("LISTENER")) {
                    while (true) {
                        System.out.println("\nListener Menu");
                        System.out.println("1. View Songs");
                        System.out.println("2. Play Song");
                        System.out.println("3. Logout");
                        SongDAO songDAO = new SongDAO();


                        int c = Integer.parseInt(sc.nextLine());

                        if (c == 1) {
                            songDAO.listAllSongs();
                        } 
                        else if (c == 2) {
                            System.out.print("Enter Song ID: ");
                            int songId = Integer.parseInt(sc.nextLine());
                            music.play(songId, user.id, sc);
                        } 
                        else {
                            break;
                        }
                    }
                }

            }
            else break;
        }
    }
}
