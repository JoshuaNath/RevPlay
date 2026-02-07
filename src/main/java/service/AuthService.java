package service;

import dao.UserDAO;
import model.User;

import java.util.Scanner;

public class AuthService {
    private final UserDAO dao = new UserDAO();

    public void register(Scanner sc) throws Exception {
        System.out.println("1. Listener  2. Artist");
        int choice = Integer.parseInt(sc.nextLine());

        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();
        System.out.print("Email: ");
        String e = sc.nextLine();
        System.out.print("Full Name: ");
        String n = sc.nextLine();

        if (choice == 1) {
            dao.registerListener(u, p, e, n);
        } else {
            System.out.print("Artist Name: ");
            String a = sc.nextLine();
            System.out.print("Genre: ");
            String g = sc.nextLine();
            dao.registerArtist(u, p, e, n, a, g);
        }
        System.out.println("✅ Registration successful");
    }

    public User login(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();
        return dao.login(u, p);
    }
}
