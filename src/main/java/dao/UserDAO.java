package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

public class UserDAO {

    // ================= REGISTER LISTENER =================
    public void registerListener(
            String u, String p, String e, String n,
            String question, String answer) throws Exception {

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
                """
                INSERT INTO users
                (username, password_hash, email, full_name, user_type,
                 security_question, security_answer)
                VALUES (?, ?, ?, ?, 'LISTENER', ?, ?)
                """,
                new String[]{"id"}
        );

        ps.setString(1, u);
        ps.setString(2, p);
        ps.setString(3, e);
        ps.setString(4, n);
        ps.setString(5, question);
        ps.setString(6, answer);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int userId = rs.getInt(1);

        PreparedStatement pls = con.prepareStatement(
                "INSERT INTO listeners(user_id) VALUES (?)");
        pls.setInt(1, userId);
        pls.executeUpdate();

        con.close();
    }

    // ================= REGISTER ARTIST =================
    public void registerArtist(
            String username,
            String password,
            String email,
            String fullName,
            String securityQuestion,
            String securityAnswer,
            String artistName,
            String genre,
            boolean disclose,
            String instagram,
            String youtube) throws Exception {

        Connection con = DBConnection.getConnection();

        // Insert into USERS (with security fields)
        PreparedStatement ps = con.prepareStatement(
                """
                INSERT INTO users(
                    username, password_hash, email, full_name,
                    user_type, security_question, security_answer
                )
                VALUES (?, ?, ?, ?, 'ARTIST', ?, ?)
                """,
                new String[]{"id"});

        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, email);
        ps.setString(4, fullName);
        ps.setString(5, securityQuestion);
        ps.setString(6, securityAnswer);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int userId = rs.getInt(1);

        // Insert into ARTISTS
        PreparedStatement pa = con.prepareStatement(
                """
                INSERT INTO artists(
                    user_id, artist_name, genre,
                    disclose_social, instagram_id, youtube_channel
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """);

        pa.setInt(1, userId);
        pa.setString(2, artistName);
        pa.setString(3, genre);
        pa.setInt(4, disclose ? 1 : 0);

        if (disclose) {
            pa.setString(5, instagram);
            pa.setString(6, youtube);
        } else {
            pa.setNull(5, Types.VARCHAR);
            pa.setNull(6, Types.VARCHAR);
        }

        pa.executeUpdate();

        con.close();
    }



    // ================= FETCH USER =================
    public User getUserByUsername(String u) throws Exception {

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                """
                SELECT id, username, password_hash, user_type,
                       security_question, security_answer
                FROM users
                WHERE username = ?
                """
        );
        ps.setString(1, u);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.id = rs.getInt("id");
            user.username = rs.getString("username");
            user.password = rs.getString("password_hash");
            user.role = rs.getString("user_type");
            user.securityQuestion = rs.getString("security_question");
            user.securityAnswer = rs.getString("security_answer");
            con.close();
            return user;
        }

        con.close();
        return null;
    }
}
