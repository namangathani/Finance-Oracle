package com.oracle;

import com.oracle.beans.Admin;
import com.oracle.beans.User;
import com.oracle.dao.AdminDAO;
import com.oracle.dao.UserDAO;
import com.oracle.dao.impl.AdminDAOImpl;
import com.oracle.dao.impl.UserDAOImpl;
import com.oracle.service.AdminService;

import java.util.Scanner;

public class FinanceMain {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		AdminDAO adminDAO = new AdminDAOImpl();
		UserDAO userDAO = new UserDAOImpl();

		//        adminDAO.printAllUsers();  // Debugging: Show all admins


		System.out.println("==== Finance Management System ====");
		System.out.println("1. Login as Admin");
		System.out.println("2. Login as User");
		System.out.print("Choose option: ");
		int choice = scanner.nextInt();
		scanner.nextLine(); // consume newline

		System.out.print("Enter username: ");
		String username = scanner.nextLine().trim();
		System.out.print("Enter password: ");
		String password = scanner.nextLine().trim();

		switch (choice) {
		case 1:
			Admin admin = adminDAO.login(username, password);
			if (admin != null) {
				System.out.println("Welcome, Admin " + admin.getName());

				boolean exit = false;
				while (!exit) {
					System.out.println("\n==== Admin Panel ====");
					System.out.println("1. View Users");
					System.out.println("2. Issue EMI Card");
					System.out.println("3. View Users with Issued EMI Cards"); // <- NEW OPTION
					System.out.println("4. Add User");
					System.out.println("5. Block an EMI Card");
					System.out.println("6. Unblock an EMI Card");
					System.out.println("7. Logout");

					System.out.print("Enter your choice: ");
					int adminChoice = scanner.nextInt();
					scanner.nextLine();

					switch (adminChoice) {
					case 1 -> AdminService.printAllUsers();
					case 2 -> AdminService.issueEMICardToUser();
					case 3 -> AdminService.showAllIssuedEMICards(); // <- NEW CALL
					case 4 -> AdminService.addUser();
					case 5 -> AdminService.blockEMICard();
					case 6 -> AdminService.unblockEMICard();
					case 7 -> {
						exit = true;
						System.out.println("👋 Logged out.");
					}
					default -> System.out.println("❌ Invalid choice.");
					}
				}

			}
			else 
			{
				System.out.println("❌ Invalid Admin Credentials");
			}
			break;
		case 2:
			User user = userDAO.login(username, password);
			if (user != null) {
				System.out.println("✅ Welcome User: " + user.getName());
			} else {
				System.out.println("❌ Invalid User Credentials or Account Inactive");
			}
			break;
		default:
			System.out.println("❌ Invalid choice.");
		}

		scanner.close();
	}
}
