package com.account.service;

import java.util.List;
import com.account.modal.UserCreditCard;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {

	Payment paymentByCreditCard(String cardId, float totalAmount, String desc) throws PayPalRESTException;
	UserCreditCard saveCreditCard(UserCreditCard creditCard);
	UserCreditCard findCreditCardById(Long id);
	
	List<UserCreditCard> findCreditCardsByUserId(Long userId);
	void deleteCreditCard(Long id);
}
