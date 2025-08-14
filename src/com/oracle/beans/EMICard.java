package com.oracle.beans;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emi_cards")
public class EMICard {

    @Id
    @Column(name = "CARD_ID")
    private Long cardId;

    @Column(nullable = false)
    private String cardType; // Silver, Gold, Platinum

    @Column(nullable = false)
    private Double creditLimit;

    private Double balance; // Amount currently used

    @Column(nullable = false)
    private String status; // Active, Blocked

    private LocalDate issueDate;
    private LocalDate expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "remaining_limit")
    private Double remainingLimit;

    // === Getters and Setters ===

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId2) {
        this.cardId = cardId2;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getRemainingLimit() {
        return remainingLimit;
    }

    public void setRemainingLimit(Double remainingLimit) {
        this.remainingLimit = remainingLimit;
    }
}
