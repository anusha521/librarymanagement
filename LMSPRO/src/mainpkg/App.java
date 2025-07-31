package mainpkg;

import java.util.Scanner;
import dao.UserDAO;
import dao.AdminDAO;
import dao.StudentDAO;
import util.DBUTIL;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;
        String currentUser = null;
        String role = null;

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Login (AA-001)");
            System.out.println("2. Change Password (AA-002)");
            System.out.println("3. Admin Actions");
            System.out.println("4. Student Actions");
            System.out.println("5. Logout (EX-001)");
            System.out.println("6. Exit ");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    if (loggedIn) {
                        System.out.println("Already logged in as " + currentUser);
                        break;
                    }
                    System.out.print("Enter userid: ");
                    String uid = sc.next();
                    System.out.print("Enter password: ");
                    String pwd = sc.next();

                    role = UserDAO.login(uid, pwd);
                    if (role != null) {
                        System.out.println("Login successful as " + role.toUpperCase());
                        loggedIn = true;
                        currentUser = uid;
                    } else {
                        System.out.println("Invalid userid/password.");
                    }
                    break;

                case 2:
                    System.out.print("Enter your User ID: ");
                    String changeUid = sc.next();
                    System.out.print("Enter new password: ");
                    String newPwd = sc.next();

                    boolean changed = UserDAO.changePassword(changeUid, newPwd);
                    System.out.println(changed ? "Password changed successfully." : "Password change failed.");
                    break;

                case 3:
                    if (!loggedIn || !role.equalsIgnoreCase("admin")) {
                        System.out.println("Only admin can access this section. Please login as admin.");
                        break;
                    }

                    while (true) {
                        System.out.println("\n--- Admin Menu ---");
                        System.out.println("1. Issue Book (AD-020)");
                        System.out.println("2. Track Issued Books (AD-021)");
                        System.out.println("3. Return Book");
                        System.out.println("4. Generate Popular Books Report (AD-024)");
                        System.out.println("5. Logout from Admin Menu (EX-001)");
                        System.out.print("Enter your choice: ");
                        int adminChoice = sc.nextInt();
                        sc.nextLine(); 

                        switch (adminChoice) {
                            case 1:
                            	System.out.print("Enter Student ID: ");
                                int bookId = sc.nextInt();
                                sc.nextLine();
                                System.out.print("Enter Book ID: ");
                                String studentId = sc.nextLine();

                                boolean issued = AdminDAO.issueBook(bookId, studentId);
                                System.out.println(issued ? "Book Issued Successfully!" : "Book Issue Failed.");
                                break;

                            case 2:
                                System.out.println("\n--- Issued Book Records ---");
                                System.out.println("SID | BookID | Issue Date | Returning Date | Returned Date");
                                System.out.println("-------------------------------------------------------------");
                                AdminDAO.viewIssuedBooks(); 
                                break;

                            case 3:
                                System.out.print("Enter Student ID: ");
                                int returnSid = sc.nextInt();
                                System.out.print("Enter Book ID: ");
                                int returnBid = sc.nextInt();
                                AdminDAO.returnBook(returnSid, returnBid);
                                break;

                            case 4:
                                AdminDAO.generatePopularBooksReport();
                                break;

                            case 0:
                                System.out.println("Logging out from admin menu...");
                                DBUTIL.closeConnection(); 
                                break;

                            default:
                                System.out.println("Invalid admin option.");
                        }

                        if (adminChoice == 0)
                            break;
                    }
                    break;

                case 4:
                    if (!loggedIn || !role.equalsIgnoreCase("student")) {
                        System.out.println("Only students can access this section. Please login as student.");
                        break;
                    }

                    while (true) {
                        System.out.println("\n--- Student Menu ---");
                        System.out.println("1. View My Issued Books");
                        System.out.println("2. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        int stuChoice = sc.nextInt();

                        switch (stuChoice) {
                            case 1:
                                StudentDAO.viewMyBooks(currentUser);
                                break;

                            case 0:
                                System.out.println("Returning to main menu...");
                                break;

                            default:
                                System.out.println("Invalid student option.");
                        }

                        if (stuChoice == 0)
                            break;
                    }
                    break;

                case 5:
                    if (loggedIn) {
                        System.out.println("Logout successful.");
                        loggedIn = false;
                        currentUser = null;
                        role = null;
                    } else {
                        System.out.println("No user is logged in.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting the application.");
                    DBUTIL.closeConnection();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
