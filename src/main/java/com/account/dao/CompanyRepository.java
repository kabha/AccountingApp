package com.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.modal.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
