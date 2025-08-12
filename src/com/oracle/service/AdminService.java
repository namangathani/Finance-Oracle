package com.oracle.service;

import com.oracle.beans.EMICard;

import com.oracle.beans.User;
import com.oracle.dao.EMICardDAO;
import com.oracle.dao.UserDAO;
import com.oracle.dao.impl.EMICardDAOImpl;
import com.oracle.dao.impl.UserDAOImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AdminService {
    private final static UserDAO userDAO = new UserDAOImpl();
    private final static EMICardDAO emiCardDAO = new EMICardDAOImpl();
    private final static Scanner sc = new Scanner(System.in);

    public static void printAllUsers() {
        try {
			userDAO.printAllUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Assuming you have this
    }

    public static void issueEMICardToUser() {
        System.out.print("Enter username to issue EMI Card: ");
        String username = sc.nextLine();

        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found.");
            return;
        }
        
        if (!"Active".equalsIgnoreCase(user.getStatus())) {
            System.out.println("❌ Cannot issue EMI Card. User status is not Active.");
            System.out.println("Current status: " + user.getStatus());
            return;
        }


        System.out.println("Select Card Type:");
        System.out.println("1. Silver (₹50,000)");
        System.out.println("2. Gold (₹1,00,000)");
        System.out.println("3. Platinum (₹2,00,000)");
        System.out.print("Choose option: ");
        int cardChoice = sc.nextInt();
        sc.nextLine(); // consume newline

        String cardType = switch (cardChoice) {
            case 1 -> "Silver";
            case 2 -> "Gold";
            case 3 -> "Platinum";
            default -> null;
        };

        if (cardType == null) {
            System.out.println("❌ Invalid selection.");
        } else {
            emiCardDAO.issueCardToUser(user, cardType);
        }
    }
    
    public static void showAllIssuedEMICards() {
        List<EMICard> cards = emiCardDAO.getAllIssuedCards();
        if (cards.isEmpty()) {
            System.out.println("❌ No EMI Cards have been issued.");
        } else {
            System.out.println("📋 List of Issued EMI Cards:");
            for (EMICard card : cards) {
                System.out.println("=======================================");
                System.out.println("👤 Username: " + card.getUser().getUsername());
                System.out.println("🔹 Name: " + card.getUser().getName());
                System.out.println("💳 Card ID: " + card.getCardId());
                System.out.println("💳 Card Type: " + card.getCardType());
                System.out.println("💰 Credit Limit: ₹" + card.getCreditLimit());
                System.out.println("📆 Issue Date: " + card.getIssueDate());
                System.out.println("⏳ Expiry Date: " + card.getExpiryDate());
                System.out.println("📌 Status: " + card.getStatus());
            }
        }
    }
    
    public static void addUser() {
        try {
            System.out.println("Enter new user details:");

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Date of Birth (yyyy-MM-dd): ");
            String dobStr = sc.nextLine();
            LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ISO_LOCAL_DATE);

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Phone Number: ");
            String phone = sc.nextLine();

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            System.out.print("Status (Active/Pending): ");
            String status = sc.nextLine();

            // Create User object and set fields
            User user = new User();
            user.setName(name);
            user.setDob(dob);
            user.setEmail(email);
            user.setPhoneNo(phone);
            user.setUsername(username);
            user.setPassword(password);  // Ideally hash this before saving in real apps
            user.setStatus(status);
            user.setCreatedAt(java.time.LocalDateTime.now());

            userDAO.addUser(user);

        } catch (Exception e) {
            System.out.println("❌ Failed to add user. Please check the input and try again.");
            e.printStackTrace();
        }
    }
    
    public static void blockEMICard() {
        System.out.print("Enter Card ID to block: ");
        long cardId = sc.nextLong();
        sc.nextLine(); // consume newline
        emiCardDAO.blockCard(cardId);
    }
    
    public static void unblockEMICard() {
        System.out.print("Enter Card ID to unblock: ");
        Long cardId = sc.nextLong();
        sc.nextLine(); // consume newline
        emiCardDAO.unblockCard(cardId);
    }



}
