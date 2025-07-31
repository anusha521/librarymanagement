package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUTIL {
    private static Connection con;

    public static Connection getConnection() throws Exception {
        if (con == null || con.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/lmsproject",  
                "root",                         
                "Skywave#2031"                   
            );
        }
        return con;
    }

    public static void closeConnection() throws Exception {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}
