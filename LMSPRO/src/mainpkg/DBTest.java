package mainpkg;

import util.DBUTIL;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try {
            Connection con = DBUTIL.getConnection();
            if (con != null) {
                System.out.println("Connected to MySQL successfully!");
                DBUTIL.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
