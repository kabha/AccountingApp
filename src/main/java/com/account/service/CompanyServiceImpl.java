package com.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.account.common.MyUtils;
import com.account.dao.CompanyRepository;
import com.account.modal.Company;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private MyUtils myUtils;
	
	public Company saveCompany(Company company) {
		company.setCreatedOn(LocalDateTime.now());
		return companyRepository.save(company);
	}

	public Boolean deleteById(Long id) {
		Boolean flag = Boolean.valueOf(false);
		try {
			companyRepository.deleteById(id);
			flag = Boolean.valueOf(true);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}
	
	public Company findCompanyById(Long id) {
		Company company = null;
		Optional<Company> list = companyRepository.findById(id);
		if (list.isPresent()) {
			company = list.get();
			company.setCreatedOnStr(myUtils.formatLocalDateTimeForUILocalDateOnly(company.getCreatedOn()));
		}
		return company;
	}

	public List<Company> findAllCompanies() {
		List<Company> companyRecords = new ArrayList<Company>();
		for(Company company: companyRepository.findAll()){
			company.setCreatedOnStr(myUtils.formatLocalDateTimeForUILocalDateOnly(company.getCreatedOn()));
			companyRecords.add(company);
		}
		return companyRecords;
	}
}
