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
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Security Question: ");
        String securityQuestion = sc.nextLine();

        System.out.print("Security Answer: ");
        String securityAnswer = sc.nextLine();

        if (choice == 1) {

            dao.registerListener(
                    username,
                    password,
                    email,
                    fullName,
                    securityQuestion,
                    securityAnswer
            );

        } else {

            System.out.print("Artist Name: ");
            String artistName = sc.nextLine();

            System.out.print("Genre: ");
            String genre = sc.nextLine();

            System.out.print("Do you want to disclose your social media accounts? (yes/no): ");
            String choiceSM = sc.nextLine();

            boolean disclose = choiceSM.equalsIgnoreCase("yes");

            String instagram = null;
            String youtube = null;

            if (disclose) {
                System.out.print("Instagram ID: ");
                instagram = sc.nextLine();

                System.out.print("YouTube Channel: ");
                youtube = sc.nextLine();
            }

            dao.registerArtist(
                    username,
                    password,
                    email,
                    fullName,
                    securityQuestion,
                    securityAnswer,
                    artistName,
                    genre,
                    disclose,
                    instagram,
                    youtube
            );
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
