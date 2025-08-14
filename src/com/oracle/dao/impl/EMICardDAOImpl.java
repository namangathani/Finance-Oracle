package com.oracle.dao.impl;

import com.oracle.beans.EMICard;
import com.oracle.beans.User;
import com.oracle.dao.EMICardDAO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class EMICardDAOImpl implements EMICardDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Finance");

    private Double getCreditLimitByType(String cardType) {
        return switch (cardType.toUpperCase()) {
            case "SILVER" -> 50000.0;
            case "GOLD" -> 100000.0;
            case "PLATINUM" -> 200000.0;
            default -> 0.0;
        };
    }
    
//    public EMICard getCardByUserId(Long userId) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            List<EMICard> cards = em.createNativeQuery("SELECT * FROM emi_cards WHERE user_id = ? AND status = 'Active'", EMICard.class)
//                .setParameter(1, userId)
//                .getResultList();
//            
//            System.out.println("📦 Cards found: " + cards.size());
//            return cards.isEmpty() ? null : cards.get(0);
//        } finally {
//            em.close();
//        }
//    }



    public EMICard getCardByUserId(Long userId) {
        EntityManager em = emf.createEntityManager();
        try {
        	TypedQuery<EMICard> query = em.createQuery(
        		    "SELECT c FROM EMICard c WHERE c.user.userId = :userId AND c.status = 'Active'", EMICard.class);
        		query.setParameter("userId", userId);
        		List<EMICard> cards = query.getResultList();
        		
        		if (!cards.isEmpty()) {
        		    System.out.println("✅ Card found: " + cards.get(0).getCardId() + " | Status: " + cards.get(0).getStatus());
        		}
        		
        		return cards.isEmpty() ? null : cards.get(0);

        } finally {
            em.close();
        }
    }

    @Override
    public List<EMICard> getAllCards() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM EMICard c", EMICard.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void blockCard(Long cardId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            EMICard card = em.find(EMICard.class, cardId);
            if (card != null) {
                card.setStatus("BLOCKED");
                System.out.println("🚫 EMI Card " + cardId + " is now blocked.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    @Override
    public void unblockCard(Long cardId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            EMICard card = em.find(EMICard.class, cardId);
            if (card != null && "Blocked".equalsIgnoreCase(card.getStatus())) {
                card.setStatus("Active");
                System.out.println("✅ EMI Card " + cardId + " has been unblocked.");
            } else {
                System.out.println("❌ Card not found or already active.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    @Override
    public void issueCardToUser(User user, String cardType) {
        Double creditLimit = getCreditLimitByType(cardType);
        
        if (creditLimit == 0.0) {
            System.out.println("❌ Invalid card type selected.");
            return;
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Random random = new Random();
        Long cardId = (long) (1000 + random.nextLong(9000)); // Generates a 4-digit number between 1000 and 9999

        // Optional: Ensure uniqueness
        while (em.find(EMICard.class, cardId) != null) {
            cardId = (Long) (1000 + random.nextLong(9000)); // Regenerate if already taken
        }

        try {
            tx.begin();

            EMICard card = new EMICard();
            card.setCardId(cardId);
            card.setUser(user);
            card.setCardType(cardType.toUpperCase());
            card.setCreditLimit(creditLimit);
            card.setBalance(0.0);
            card.setStatus("Active");
            card.setIssueDate(LocalDate.now());
            card.setExpiryDate(LocalDate.now().plusYears(3));
            card.setRemainingLimit(creditLimit); // At the time of card issuance


            em.persist(card);
            tx.commit();

            // 🎉 Show full card details after issuing
            System.out.println("✅ EMI Card Issued Successfully:");
            System.out.println("💳 Card ID: " + cardId);
            System.out.println("🔹 Card Type: " + card.getCardType());
            System.out.println("🔹 Credit Limit: ₹" + card.getCreditLimit());
            System.out.println("🔹 Remaining Limit: ₹" + card.getRemainingLimit());
            System.out.println("🔹 Issued To: " + user.getName() + " (" + user.getUsername() + ")");
            System.out.println("🔹 Issue Date: " + card.getIssueDate());
            System.out.println("🔹 Expiry Date: " + card.getExpiryDate());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }



	@Override
	public void issueCardToUser(User user, String cardType, Double creditLimit) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<EMICard> getAllIssuedCards() {
	    EntityManager em = emf.createEntityManager();
	    try {
	        return em.createQuery("SELECT c FROM EMICard c", EMICard.class)
	                 .getResultList();
	    } finally {
	        em.close();
	    }
	}

}

