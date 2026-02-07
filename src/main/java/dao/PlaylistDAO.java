package dao;

import util.DBConnection;
import java.sql.*;

public class PlaylistDAO {

    public void createPlaylist(int userId, String name) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO playlists(listener_id,name) VALUES (?,?)");
        ps.setInt(1, userId);
        ps.setString(2, name);
        ps.executeUpdate();
        con.close();
    }
}
