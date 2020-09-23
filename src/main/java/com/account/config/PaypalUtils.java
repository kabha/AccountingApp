package com.account.config;

import com.account.common.AppConstants;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.CreditCardToken;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaypalUtils {

	private APIContext apiContext;
	private String mode = "sandbox";
	private String clientID = "ATsCy40jJYzfhUqQTWovQUCo64sd2QT3oFN1atJgZdyJIzSiquITUTWE1q7M34lHosiOwy2_7voUK_n3";
	private String clientSecret = "EP7IAd8iLSglvTFNHDIOzcrgS2M-9tkr9-hW9XsTr5fON-Nxrc000-IhmToyOTcZG7Gdap_kG1ypg6D3";

	private static String amexRegex = "^3[47][0-9]{13}$";
	private static String dinnerClubRegex = "^3(?:0[0-59]{1}|[689])[0-9]{0,}$";
	private static String discoverRegex = "^6(?:011|5[0-9]{2})[0-9]{12}$";
	private static String jcbRegex = "^(?:2131|1800|35[0-9]{3})[0-9]{11}$";	
	private static String maestroRegex = "^(5[06789]|6)[0-9]{0,}$";
	private static String masterRegex = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$";
	private static String rupayRegex = "^6(?:0|521|522[0-9]{12,14})?$";
	private static String visaRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
	private static final Logger logger = LoggerFactory.getLogger(PaypalUtils.class);

	public APIContext getApiContext() {
		if (apiContext == null)
			apiContext = new APIContext(clientID, clientSecret, mode);
		apiContext.setRequestId(null);
		return apiContext;
	}

	public CreditCard createCreditCard(CreditCard creditCard) throws PayPalRESTException {
		System.out.println("Hello Create CreditCard Paypal");
		try {
			//APIContext apiContext = getApiContext();
			creditCard.setExpireYear(calculateExpiryYear(creditCard.getExpireYear()));
			creditCard.setType(getCreditCardType(creditCard.getNumber()));
			CreditCard credit= new CreditCard();
			System.out.println("Created-Card-Response: " + CreditCard.getLastResponse());
			APIContext context = new APIContext(clientID, clientSecret, "sandbox");
			credit = creditCard.create(context);
			System.out.println(creditCard.toJSON());
			return credit;
		}catch (PayPalRESTException e) {
			System.err.println(e.getDetails());return null; 
		}
	}

	public CreditCard getCreditCardDetails(String cardId) throws PayPalRESTException {
		APIContext apiContext = getApiContext();
		return CreditCard.get(apiContext, cardId);
	}

	public void deleteCreditCard(String cardId) throws PayPalRESTException {
		APIContext apiContext = getApiContext();
		CreditCard creditCard = CreditCard.get(apiContext, cardId);
		creditCard.delete(apiContext);
	}

	public Payment createPaymentFromCreditCard(String cardId, float totalAmount, String desc) throws PayPalRESTException {
		Payment payment = new Payment();
		try {
			String totalAmountStr = String.format("%.2f", totalAmount);
			APIContext apiContext = getApiContext();//new APIContext(clientID, clientSecret, "sandbox");
			Amount amount = new Amount();
			amount.setCurrency(AppConstants.CURRENCY);
			amount.setTotal(totalAmountStr);
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setDescription(desc);
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);
			FundingInstrument fundingInstrument = new FundingInstrument();
			fundingInstrument.setCreditCardToken(new CreditCardToken(cardId));
			List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
			fundingInstruments.add(fundingInstrument);
			Payer payer = new Payer();
			payer.setFundingInstruments(fundingInstruments);
			payer.setPaymentMethod(AppConstants.PAYMENT_METHOD_CREDIT_CARD);
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			System.out.println("Payment: " + Payment.getLastResponse());
			//APIContext context = new APIContext(clientID, clientSecret, "sandbox");
			payment.create(apiContext);
			System.out.println(payment.toJSON());
			return payment;
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());return null; 
		}

	}

	private String getCreditCardType(String cardNumber) {
		String result = null;
		if (cardNumber.matches(amexRegex)) {
			result = "amex";
		} else if (cardNumber.matches(dinnerClubRegex)) {
			result = "dinner";
		} else if (cardNumber.matches(discoverRegex)) {
			result = "discover";
		} else if (cardNumber.matches(jcbRegex)) {
			result = "jcb";
		} else if (cardNumber.matches(maestroRegex)) {
			result = "maestro";
		} else if (cardNumber.matches(masterRegex)) {
			result = "mastercard";
		} else if (cardNumber.matches(rupayRegex)) {
			result = "rupay";
		} else if (cardNumber.matches(visaRegex)) {
			result = "visa";
		}

		System.out.println("the creditcard type is "+result);
		return result;
	}

	private Integer calculateExpiryYear(Integer expiryYear) {
		System.out.println("Inside the calculateExpiryYear");
		System.out.println(expiryYear);

		String expiryYearStr = String.valueOf(expiryYear);
		if(expiryYear<10) {
			expiryYearStr = "0"+expiryYearStr;
		}
		if(expiryYear < 100) {
			String year = LocalDate.now().getYear()+"";
			Integer currCentury = Integer.parseInt(year.substring(0, 2));
			Integer currYear = Integer.parseInt(year.substring(2));
			if(currYear <= expiryYear) {
				expiryYearStr = currCentury + expiryYearStr;
			}else if(currYear+20 < expiryYear) {
				expiryYearStr = "00";
			}else {
				expiryYearStr = currCentury+1+ expiryYearStr;
			}
		}
		System.out.println(Integer.parseInt(expiryYearStr));
		return Integer.parseInt(expiryYearStr);
	}

	public static void main(String[] args) throws PayPalRESTException {
		PaypalUtils paypal = new PaypalUtils();
		CreditCard creditCard = new CreditCard();
		creditCard.setNumber("45033333222222");
		creditCard.setExpireMonth(12);
		creditCard.setExpireYear(2023);
		creditCard.setType("mastercard");
		creditCard.setCvv2("444");

		String cardID = "CARD-89545574WU620321EL3C4THI";

		CreditCard cc = paypal.createCreditCard(creditCard);
		System.out.println(cc.getValidUntil());

		paypal.createCreditCard(creditCard);
		//		paypal.createBillingPlan();
		//		paypal.getBillingPlan();
		//		paypal.createBillingAgreement();
		paypal.createPaymentFromCreditCard(cardID, 2.5f, "test");

	}
}
