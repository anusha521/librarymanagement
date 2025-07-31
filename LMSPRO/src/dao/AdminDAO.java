package dao;

import java.sql.*;
import java.util.Calendar;
import util.DBUTIL;

public class AdminDAO {
    public static boolean issueBook(int book_id, String student_id) {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement checkStmt = con.prepareStatement(
                "SELECT available_copies FROM books WHERE book_id = ?"
            );
            checkStmt.setInt(1, book_id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int available = rs.getInt("available_copies");
                if (available > 0) {
                    java.sql.Date issueDate = new java.sql.Date(System.currentTimeMillis());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(issueDate);
                    cal.add(Calendar.DAY_OF_MONTH, 21);
                    java.sql.Date returningDate = new java.sql.Date(cal.getTimeInMillis());
                    PreparedStatement issueStmt = con.prepareStatement(
                        "INSERT INTO book_issues(book_id, student_id, issue_date, returning_date) VALUES (?, ?, ?, ?)"
                    );
                    issueStmt.setInt(1, book_id);
                    issueStmt.setString(2, student_id);
                    issueStmt.setDate(3, issueDate);
                    issueStmt.setDate(4, returningDate);
                    issueStmt.executeUpdate();
                    PreparedStatement updateStmt = con.prepareStatement(
                        "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ?"
                    );
                    updateStmt.setInt(1, book_id);
                    updateStmt.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("[Error] Issue Book: " + e.getMessage());
        }
        return false;
    }
    public static void viewIssuedBooks() {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM book_issues");
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- Issued Books ---");
            while (rs.next()) {
                System.out.println("Issue ID: " + rs.getInt("issue_id") +
                        ", Book ID: " + rs.getInt("book_id") +
                        ", Student ID: " + rs.getString("student_id") +
                        ", Issue Date: " + rs.getDate("issue_date") +
                        ", Due Date: " + rs.getDate("returning_date") +
                        ", Returned Date: " + rs.getDate("returned_date"));
            }
        } catch (Exception e) {
            System.out.println("[Error] View Issued Books: " + e.getMessage());
        }
    }
    public static void returnBook(int studentId, int bookId) {
        try {
            Connection con = DBUTIL.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE book_issues SET returned_date = CURRENT_DATE WHERE student_id = ? AND book_id = ? AND returned_date IS NULL"
            );
            ps.setInt(1, studentId);
            ps.setInt(2, bookId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                PreparedStatement updateBook = con.prepareStatement(
                    "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?"
                );
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();
                System.out.println("✅ Book returned successfully!");
            } else {
                System.out.println("❌ No active issue found for given student and book.");
            }
        } catch (Exception e) {
            System.out.println("[Error] Return Book: " + e.getMessage());
        }
    }
    public static void generatePopularBooksReport() {
        try {
            Connection con = DBUTIL.getConnection();
            String sql = "SELECT book_id, COUNT(*) AS issue_count FROM book_issues GROUP BY book_id ORDER BY issue_count DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- Popular Books Report ---");
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id") + ", Times Issued: " + rs.getInt("issue_count"));
            }
        } catch (Exception e) {
            System.out.println("[Error] Generate Report: " + e.getMessage());
        }
    }
}
