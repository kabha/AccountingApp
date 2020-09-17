package com.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.account.modal.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

	@Query("SELECT u.emailId FROM AppUser u WHERE u.id = :id")
	String getEmailIdByUserId(@Param("id") Long id);
	
	AppUser findByEmailId(String emailId);
    
    boolean existsByEmailId(String emailId);
	boolean existsByPhoneNumber(String phoneNumber);

}
