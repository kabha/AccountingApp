package com.account.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.account.modal.UserCreditCard;

public interface CreditCardRepository  extends JpaRepository<UserCreditCard, Long> {

	List<UserCreditCard> findByUserId(Long userId);
}
