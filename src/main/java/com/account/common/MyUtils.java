package com.account.common;

import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyUtils {
	
	@Autowired
	private HttpServletRequest request;
	
	private static String local_Date_Format = "dd/MM/yyyy";
	private static String local_DateTime_Format = "dd/MM/yyyy HH:mm:ss";
	private static String dateFormatForFileName = "ddMMyyyy_HHmmssSSS";
	private static String DB_DateTime_Format = "yyyy-MM-dd HH:mm:ss";
	private static String humanReadable_Format = "dd MMMM yyyy";
	private static SecretKeySpec secretKeySpec;
	private static byte[] digestKey;
	
	static {
		try {
			
			String secretKey = "abcdefGHIJKLmnopqrsUVXYZ1234567890";
			digestKey = MessageDigest.getInstance("SHA-1").digest(secretKey.getBytes("UTF-8"));
			digestKey = Arrays.copyOf(digestKey, 16);
			secretKeySpec = new SecretKeySpec(digestKey, "AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String pwd) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(1, secretKeySpec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(pwd.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
			return null;
		}
	}

	public String decrypt(String encryptedPwd) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(2, secretKeySpec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedPwd)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
			return null;
		}
	}
	
	
	public String encryptURLOnly(String str) {
//        String encriptedStr = Base64.getEncoder().encodeToString("123".getBytes());
//        String decriptedStr = new String(Base64.getDecoder().decode(encriptedStr));
		
		String encryptedStr = encrypt(str);
		if(encryptedStr.contains("/")) {
			encryptedStr = Base64.getUrlEncoder().encodeToString(encryptedStr.getBytes())+"_URL0";
		}
        return encryptedStr;
	}
	
	public String decryptURLOnly(String encryptedURL) {
		if(encryptedURL.endsWith("_URL0")) {
			encryptedURL = new String(Base64.getUrlDecoder().decode(encryptedURL.replace("_URL0", "")));
		}
        return decrypt(encryptedURL);
	}
	
	public String getServerBaseURL() {
		String url = request.getRequestURL().toString();
		if(url.contains(AppConstants.APP_PORT_NUMBER)) {
			url = url.substring(0, url.indexOf(AppConstants.APP_PORT_NUMBER)+4);
		}else {
			url = url.substring(0, url.indexOf(".com")+4);
		}
		return url;
	}
	
	public String getServletContextPath() {
		return request.getServletContext().getRealPath("/");
	}

//	===================================DateTime-Formating-Start=====================================
	public LocalDateTime parseLocalDateTimeForDB(String dateTime) {
		return LocalDateTime.parse(dateTime.replace("Z", ""));
	}
	
	public String getTodayDate_ForFileName() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormatForFileName));
	}

	public String formatLocalDateTimeForUISlashDate(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern(local_Date_Format));
	}
	public String formatLocalDateForUI(LocalDate date) {
		String dateStr = date.format(DateTimeFormatter.ofPattern(local_Date_Format));
		return dateStr.replace("/", ".");
	}
	public String formatLocalDateForUILocalDateOnly(String date) {
		String dateStr = LocalDate.parse(date).format(DateTimeFormatter.ofPattern(local_Date_Format));
		return dateStr.replace("/", ".");
	}
	
	public String formatLocalDateTimeForUI(LocalDateTime dateTime) {
		String dateTimeStr = dateTime.format(DateTimeFormatter.ofPattern(local_DateTime_Format));
		dateTimeStr = dateTimeStr.substring(0, dateTimeStr.lastIndexOf(":"));
		return dateTimeStr.replace("/", ".");
	}
	public String formatLocalDateTimeForUI(String dateTime) {
		String dateTimeStr = LocalDate.parse(dateTime).format(DateTimeFormatter.ofPattern(local_DateTime_Format));
		dateTimeStr = dateTimeStr.substring(0, dateTimeStr.lastIndexOf(":"));
		return dateTimeStr.replace("/", ".");
	}
	public String formatLocalDateTimeForUILocalDateOnly(String dateTime) {
		String dateTimeStr = LocalDate.parse(dateTime).format(DateTimeFormatter.ofPattern(local_Date_Format));
		return dateTimeStr.replace("/", ".");
	}
	public String formatLocalDateTimeForUILocalDateOnly(LocalDateTime dateTime) {
		String dateTimeStr = dateTime.format(DateTimeFormatter.ofPattern(local_Date_Format));
		return dateTimeStr.replace("/", ".");
	}
	
	public String formatLocalDateTimeForHuman(String dateTime) {
		return LocalDateTime.parse(dateTime).format(DateTimeFormatter.ofPattern(humanReadable_Format));
	}
	
	public String getTodayDate_ForPaypal() {
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DB_DateTime_Format));
		return dateTime.replaceFirst("\\s", "T")+"Z";
	}
	
	public Long getSecondsFromTodayDateTime() {
		return System.currentTimeMillis()/1000;
	}
	public LocalDateTime getLocalDateTimeFromTimeInSeconds(String timeInSeonds) {
		Long timeInMillis = Long.parseLong(timeInSeonds+"000");
//		Date date = new Date(timeInMillis);
		Instant instant = Instant.ofEpochMilli(timeInMillis);
	    return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public long getDaysFromGivenDateTimeByToday(LocalDateTime dateTime) {
//		long hoursDiff = ChronoUnit.HOURS.between(dateTime, LocalDateTime.now());
		return ChronoUnit.DAYS.between(dateTime, LocalDateTime.now());
	}
	
//	===================================DateTime-Formating-End=====================================

	public boolean validatePhoneNumber(String phoneNumber) {
//		return phoneNumber.matches("\\+\\d{12,15}");
		return phoneNumber.matches("\\d{10,12}");
	}
	
	public boolean validateEmail(String email) {
		String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
		return email.matches(EMAIL_REGEX);
	}
	
	// It will generate 2 digit random Number from 0 to 99
	public String get2DigitRandomNumber() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(99);
	    return String.format("%02d", number);
	}
	public String get6DigitRandomNumber() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    // this will convert any number sequence into 6 character.
	    return String.format("%06d", number);
	}
	
	public Double parseDoubleUpto2DecimalPlaces(Double doubleValue) {
//		float floatValue = 22.3453555f;
		if(doubleValue != null) {
			return Double.parseDouble(String.format("%.2f", doubleValue));
		}else {
			return doubleValue;
		}
	}
	
	public String parseDoubleUpto2DecimalPlaces(String decimalValue) {
		if(decimalValue==null) decimalValue = "0";
		if(decimalValue.indexOf("E")>0) {
			decimalValue = decimalValue.substring(0, decimalValue.indexOf("E"));
		}
		else if(decimalValue.indexOf("e")>0) {
			decimalValue = decimalValue.substring(0, decimalValue.indexOf("e"));
		}
		return String.format("%.2f", Double.parseDouble(decimalValue));
	}
	
	/**
	 * It Return Empty string if passed parameter is NULL
	 * @param str
	 */
	public String avoidNullableValue(String str) {
		return str!=null?str:"";
	}
	public Integer avoidNullableValue(Integer number) {
		return number!=null?number:0;
	}
	public Long avoidNullableValue(Long number) {
		return number!=null?number:0;
	}
	public Float avoidNullableValue(Float number) {
		return number!=null?number:0;
	}
	public Double avoidNullableValue(Double number) {
		return number!=null?number:0;
	}
	
	private void sortMyList() {
//		Collections.sort(list, (loc1,loc2)->{ return loc1.getTotalStations() < loc2.getTotalStations()?1:-1; });
	}
	
	public String getOrderId() {
		String base = "0123456789ABCDEFGHIJKLmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 15; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		MyUtils myUtils = new MyUtils();
		String pwd = "Bearer eyJhbGciOiJIUzI1NiJ9";
		System.out.println(pwd.substring(7));
		
	}

}
