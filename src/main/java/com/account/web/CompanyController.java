package com.account.web;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.common.AppConstants;
import com.account.modal.Company;
import com.account.service.CompanyService;

@RestController
@RequestMapping({ "/api/companies" })
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	private String Status = "status";
	private String Message = "message";

	private Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@GetMapping("")
	public String getCompanies() {
		logger.info("-------------getCompanies---------------");
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
			jsonResponse.put("companies", companyService.findAllCompanies());
		} catch (Exception ex) {
			jsonResponse.put(Status, 0);
			jsonResponse.put(Message, AppConstants.SOMETHING_WRONG_MSG);
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@GetMapping("/find/{id}")
	public Company getCompanyById(@PathVariable("id") Long id) {
		return companyService.findCompanyById(id);
	}
	
	@PostMapping(value = {"/save"}, consumes = {"application/json"})
	public String saveCompany(@RequestBody Company company) {
		logger.info("-------------saveCompany---------------");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put(Status, 0);
		jsonResponse.put(Message, AppConstants.SOMETHING_WRONG_MSG);
		try {
			companyService.saveCompany(company);
			jsonResponse.put(Status, 1);
			jsonResponse.put(Message, AppConstants.SUCCESS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonResponse.toString();
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteCompany(@PathVariable("id") Long id) {
		System.out.println("------deleteCompany-----");
		companyService.deleteById(id);
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put(Status, 1);
		jsonResponse.put(Message, AppConstants.SUCCESS);
		return jsonResponse.toString();
	}
}
