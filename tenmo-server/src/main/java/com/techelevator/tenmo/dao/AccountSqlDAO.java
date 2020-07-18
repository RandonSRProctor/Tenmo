package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service
public class AccountSqlDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public AccountSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public BigDecimal getBalanceByUsername(String username) {
    	String sql = "SELECT balance FROM accounts WHERE user_id = "
    			+ "(SELECT user_id FROM users WHERE username = ?);";
    	SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
    	BigDecimal balance = BigDecimal.ZERO;
    	
    	if(results.next()) {
    		balance = new BigDecimal(results.getDouble("balance"));
    	}
    	return balance;
    }
	
	
}
