package dao;

import util.DBConnection;
import java.sql.*;

public class ArtistDAO {

    public void viewStats(int artistId) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT title,play_count FROM songs WHERE artist_id=?");
        ps.setInt(1, artistId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) +
                    " | Plays: " + rs.getInt(2));
        }
        con.close();
    }
}
