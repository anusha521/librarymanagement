package dao;

import java.sql.*;
import util.DBUTIL;

public class StudentDAO {
    public static void viewMyBooks(String currentUserId) {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement ps1 = con.prepareStatement("SELECT role FROM users WHERE user_id = ?");
            ps1.setString(1, currentUserId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                System.out.println("User not found.");
                return;
            }

            String role = rs1.getString("role");
            if (!"student".equalsIgnoreCase(role)) {
                System.out.println("Only students can view issued books.");
                return;
            }
            PreparedStatement ps = con.prepareStatement(
                "SELECT b.book_id, b.title, bi.issue_date, bi.returning_date, bi.returned_date " +
                "FROM books b JOIN book_issues bi ON b.book_id = bi.book_id " +
                "WHERE bi.student_id = ?"
            );
            ps.setString(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- 📚 My Issued Books ---");
            System.out.printf("%-10s %-30s %-15s %-20s %-20s\n", "Book ID", "Title", "Issue Date", "Expected Return", "Actual Return");
            System.out.println("------------------------------------------------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                Date issueDate = rs.getDate("issue_date");
                Date expectedReturn = rs.getDate("returning_date");  // fixed column name
                Date actualReturn = rs.getDate("returned_date");

                System.out.printf("%-10d %-30s %-15s %-20s %-20s\n",
                        bookId, title, issueDate, expectedReturn,
                        (actualReturn != null ? actualReturn.toString() : "Not Returned Yet"));
            }
            if (!found) {
                System.out.println("No books issued.");
            }
        } catch (Exception e) {
            System.out.println("[Error] View My Books: " + e.getMessage());
        }
    }
}
