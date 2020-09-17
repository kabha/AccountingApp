package com.account.service;

import java.util.List;

import com.account.modal.Company;

public interface CompanyService {

	Company saveCompany(Company company);
	Boolean deleteById(Long id);
	Company findCompanyById(Long id);
	List<Company> findAllCompanies();
}
