package com.oracle.dao;

import com.oracle.beans.EMICard;
import com.oracle.beans.User;

import java.util.List;

public interface EMICardDAO {
    void issueCardToUser(User user, String cardType, Double creditLimit);
    EMICard getCardByUserId(Long userId);
    List<EMICard> getAllCards();
    void blockCard(Long cardId);
	void issueCardToUser(User user, String cardType);
	List<EMICard> getAllIssuedCards();
	void unblockCard(Long cardId); // Use Integer or Long based on your EMICard enti
}
