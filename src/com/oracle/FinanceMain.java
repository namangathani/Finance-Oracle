package com.oracle;

import com.oracle.beans.Admin;
import com.oracle.beans.EMICard;
import com.oracle.beans.Product;
import com.oracle.beans.Purchase;
import com.oracle.beans.PurchaseItem;
import com.oracle.beans.Transaction;
import com.oracle.beans.User;
import com.oracle.dao.AdminDAO;
import com.oracle.dao.EMICardDAO;
import com.oracle.dao.ProductDAO;
import com.oracle.dao.PurchaseDAO;
import com.oracle.dao.TransactionDAO;
import com.oracle.dao.UserDAO;
import com.oracle.dao.impl.AdminDAOImpl;
import com.oracle.dao.impl.EMICardDAOImpl;
import com.oracle.dao.impl.ProductDAOImpl;
import com.oracle.dao.impl.PurchaseDAOImpl;
import com.oracle.dao.impl.TransactionDAOImpl;
import com.oracle.dao.impl.UserDAOImpl;
import com.oracle.service.AdminService;

import java.util.List;
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
					System.out.println("7. View All Transactions");
					System.out.println("8. Logout");

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
					    TransactionDAO transactionDAO = new TransactionDAOImpl();
					    List<Transaction> allTransactions = transactionDAO.getAllTransactions();

					    if (allTransactions.isEmpty()) {
					        System.out.println("No transactions found.");
					    } else {
					        System.out.println("\n--- All Transactions ---");
					        for (Transaction t : allTransactions) {
					            System.out.printf("Transaction ID: %d | User ID: %d | Purchase ID: %s | Amount: ₹%.2f | Type: %s | Date: %s\n",
					                    t.getTransactionId(),
					                    t.getUser().getUserId(),
					                    (t.getPurchase() != null ? t.getPurchase().getPurchaseId() : "N/A"),
					                    t.getAmount(),
					                    t.getTransactionType(),
					                    t.getTransactionDate().toString());
					        }
					        System.out.println("-------------------------");
					    }
					}

					case 8 -> {
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
				boolean userRunning = true;
				while (userRunning) {
					System.out.println("\n==== User Menu ====");
					System.out.println("1. View Profile");
					System.out.println("2. View EMI Card Details");
					System.out.println("3. View Product Catalog");
					System.out.println("4. Buy Product");
					System.out.println("5. View My Purchases");
					System.out.println("6. Logout");
					System.out.print("Choose: ");
					int userChoice = scanner.nextInt();
					scanner.nextLine();

					switch (userChoice) {
					case 1 -> {
						System.out.println("\n--- Profile ---");
						System.out.println("Name: " + user.getName());
						System.out.println("DOB: " + user.getDob());
						System.out.println("Email: " + user.getEmail());
						System.out.println("Phone: " + user.getPhoneNo());
						System.out.println("Status: " + user.getStatus());
					}
					case 2 -> {
						EMICardDAO cardDAO = new EMICardDAOImpl();
						EMICard card = cardDAO.getCardByUserId(user.getUserId());
//						EMICard card = EMICardDAO.getCardByUserId(user.getUserId());
						if (card != null) {
							System.out.println("\n--- EMI Card Details ---");
							System.out.println("Card ID: " + card.getCardId());
							System.out.println("Card Type: " + card.getCardType());
							System.out.println("Limit: " + card.getCreditLimit());
							System.out.println("Remaining Limit: " + card.getRemainingLimit());
//							System.out.println("Joining Fee: " + card.getJoiningFee());
							System.out.println("Valid Till: " + card.getExpiryDate());
							System.out.println("Status: " + card.getStatus());
						} else {
							System.out.println("❌ No EMI card found.");
						}
					}
					case 3 -> {
						ProductDAO productDAO = new ProductDAOImpl();
						List<Product> list = productDAO.getAllProducts();

						if (list.isEmpty()) {
							System.out.println("No products available.");
						} else {
							System.out.println("\n--- Product Catalog ---");
							for (Product pr : list) {
								System.out.printf("%d. %s | %s | ₹%.2f\n",
										pr.getProductId(),
										pr.getName(),
										pr.getCategory(),
										pr.getPrice());
								System.out.println("   " + pr.getDescription());
							}
						}
					}
					case 4 -> {
						EMICardDAO card = new EMICardDAOImpl();
						EMICard card1 = card.getCardByUserId(user.getUserId());
						if (card1 == null || !"Active".equalsIgnoreCase(card1.getStatus())) {
							System.out.println("❌ You do not have an active EMI card.");
							break;
						}
						ProductDAO productDAO = new ProductDAOImpl();
						List<Product> prods = productDAO.getAllProducts();
						if (prods.isEmpty()) {
							System.out.println("❌ No products available.");
							break;
						}
						System.out.println("\n--- Product Catalog ---");
						for (Product p : prods) {
							System.out.printf("%d. %s | %s | ₹%.2f\n",
									p.getProductId(), p.getName(), p.getCategory(), p.getPrice());
							System.out.println("   " + p.getDescription());
						}
						System.out.print("Enter Product ID to buy: ");
						long pid = scanner.nextLong();
						scanner.nextLine();
						Product selected = productDAO.getProductById(pid);
						if (selected == null) {
							System.out.println("❌ Invalid product ID.");
							break;
						}
						System.out.print("Enter EMI Tenure (3/6/9/12 months): ");
						int tenure = scanner.nextInt();
						scanner.nextLine();
						if (!(tenure == 3 || tenure == 6 || tenure == 9 || tenure == 12)) {
							System.out.println("❌ Invalid tenure.");
							break;
						}
						System.out.print("Confirm purchase of " + selected.getName() +
								" for ₹" + selected.getPrice() + "? (y/n): ");
						String confirm = scanner.nextLine();
						if (confirm.equalsIgnoreCase("y")) {
							PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
							purchaseDAO.createPurchase(user, card1, List.of(selected), tenure);
						} else {
							System.out.println("Purchase cancelled.");
						}
					}
					case 5 -> {
					    PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
					    List<Purchase> purchases = purchaseDAO.getPurchasesByUserId(user.getUserId());

					    if (purchases.isEmpty()) {
					        System.out.println("No purchases found.");
					    } else {
					        for (Purchase p : purchases) {
					            System.out.printf("📦 Purchase ID: %d | Total: ₹%.2f | Tenure: %d mo | Status: %s\n",
					                    p.getPurchaseId(),
					                    p.getTotalAmount() + p.getProcessingFee(),
					                    p.getTenureMonths(),
					                    p.getStatus());
					            System.out.println("🗓 Date: " + p.getPurchaseDate());
					            System.out.println("🛒 Items:");
					            for (PurchaseItem item : p.getItems()) {
					                System.out.printf("   - %s (₹%.2f)\n", item.getProduct().getName(), item.getPrice());
					            }
					            System.out.println("----------------------------------");
					        }
					    }
					}

					case 6 -> userRunning = false;
					
					default -> System.out.println("❌ Invalid choice.");
					}
				}
			}

			else {
				System.out.println("❌ Invalid User Credentials or Account Inactive");
			}
			break;
		default:
			System.out.println("❌ Invalid choice.");
		}

		scanner.close();
	}
}

