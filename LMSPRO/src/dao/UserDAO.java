package dao;

import java.sql.*;
import util.DBUTIL;

public class UserDAO {

    public static String login(String uid, String pwd) {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT role FROM users WHERE user_id=? AND password=?");
            ps.setString(1, uid);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) {
            System.out.println("[Login Error] " + e.getMessage());
        }
        return null;
    }
    public static boolean changePassword(String uid, String newPwd) {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE users SET password=? WHERE user_id=?");
            ps.setString(1, newPwd);
            ps.setString(2, uid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("[Password Change Error] " + e.getMessage());
        }
        return false;
    }
}
