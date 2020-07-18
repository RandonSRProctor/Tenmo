package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountsController {
	private AccountDAO accountDAO;
	
	
	// AccountsController will allow the server api to access the sql database accounts table
	public AccountsController(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}
	
	// request mapping to access getBalanceByUsername() method we created
	// Use postman to double check everything is working
	// Implement client side that calls this api method

	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {
		return accountDAO.getBalanceByUsername(principal.getName());
	}
	
	// Method to receive transfer data from client, deserialize JSON object,
	// create transfer object (model data), pass off transfer object to ADO to alter SQL database
}
