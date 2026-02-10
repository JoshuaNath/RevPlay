package service;

import dao.UserDAO;
import model.User;

import java.util.Scanner;

public class AuthService {

    private final UserDAO dao = new UserDAO();

    // ================= REGISTER =================
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
        System.out.print("Security Question: ");
        String q = sc.nextLine();
        System.out.print("Security Answer: ");
        String a = sc.nextLine();
        if (choice == 1) {
            dao.registerListener(u, p, e, n, q, a);
        } else {
            System.out.print("Artist Name: ");
            String artistName = sc.nextLine();
            System.out.print("Genre: ");
            String genre = sc.nextLine();

            dao.registerArtist(u, p, e, n, artistName, genre, q, a);
        }
        System.out.println("Registration successful");
    }

    // ================= LOGIN =================
    public User login(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();
        User user = dao.getUserByUsername(u);
        if (user == null) {
            return null;
        }
        if (user.password.equals(p)) {
            return user;
        }
        System.out.println("Password incorrect.");
        System.out.println("Security Question:");
        System.out.println(user.securityQuestion);

        System.out.print("Answer: ");
        String inputAnswer = sc.nextLine();

        if (matchesWordByWord(user.securityAnswer, inputAnswer)) {
            System.out.println("Security answer verified");
            return user;
        }

        System.out.println("Security answer incorrect");
        return null;
    }


    private boolean matchesWordByWord(String actual, String input) {
        String[] a = actual.trim().toLowerCase().split("\\s+");
        String[] b = input.trim().toLowerCase().split("\\s+");
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }
}
