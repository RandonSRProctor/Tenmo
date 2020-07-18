package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
	
	BigDecimal getBalanceByUsername(String username);
	

}
