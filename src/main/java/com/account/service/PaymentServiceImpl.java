package com.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.common.MyUtils;
import com.account.config.PaypalUtils;
import com.account.dao.CreditCardRepository;
import com.account.modal.UserCreditCard;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaypalUtils paypalUtils;
	
	@Autowired
	private MyUtils myUtils;
	
	@Autowired
	private CreditCardRepository cardRepository;
	
	//===============================Payment=====================================
	public Payment paymentByCreditCard(String cardId, float totalAmount, String desc) throws PayPalRESTException {
		return paypalUtils.createPaymentFromCreditCard(cardId, totalAmount, desc);
	}

	//===============================CreditCard2=====================================
	public UserCreditCard saveCreditCard(UserCreditCard card) {
		System.out.println("Inside the saveCreditCard");
		UserCreditCard savedCard = null;
		CreditCard creditCard = new CreditCard();
		
		creditCard.setNumber(card.getCardNumber());
		creditCard.setExpireMonth(card.getExpiryMonth());
		creditCard.setExpireYear(card.getExpiryYear());
		creditCard.setCvv2(card.getCvv2());
		System.out.println("the Card Details" +card.getCardNumber());
		try {
			CreditCard createdCard = paypalUtils.createCreditCard(creditCard);
			if(createdCard != null) {
				card.setCardId(createdCard.getId());
				card.setCardNumberFull(card.getCardNumber())/*)*/;
				card.setCardNumber(createdCard.getNumber())/*)*/;
				card.setExpiryYear(createdCard.getExpireYear());
				card.setType(createdCard.getType());
				
				card.setCreatedOn(LocalDateTime.now());
				
				savedCard = cardRepository.save(card);
			}
		} catch (PayPalRESTException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return savedCard;
	}

	public UserCreditCard findCreditCardById(Long id) {
		Optional<UserCreditCard> list = cardRepository.findById(id);
		if (list.isPresent())
			return (UserCreditCard) list.get();
		return null;
	}
	
	public List<UserCreditCard> findCreditCardsByUserId(Long userId) {
		return cardRepository.findByUserId(userId);
	}
	
	public void deleteCreditCard(Long id) {
		cardRepository.deleteById(id);
	}
	
}
