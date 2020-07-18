package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.techelevator.tenmo.model.Transfer;

@Service
public class TransferSqlDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public TransferSqlDAO(JdbcTemplate jdbcTemplate) {
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

	@Override
	public boolean isTransferAllowed(String usernameFrom, BigDecimal amountToTransfer) {
		boolean isAllowed = amountToTransfer.compareTo(getBalanceByUsername(usernameFrom)) == -1
				|| amountToTransfer.compareTo(getBalanceByUsername(usernameFrom)) == 0;
		
		return isAllowed;
	}
	
	@Override
	public void initiateTransfer(Transfer currentTransfer) {
		if (isTransferAllowed(currentTransfer.getAccountFrom(), currentTransfer.getAmount())) {
			String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)\r\n" + 
					"VALUES ((SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = 'Send'), \r\n" + 
					"        (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Approved'), \r\n" + 
					"        (SELECT account_id FROM users INNER JOIN accounts ON accounts.user_id = users.user_id WHERE username = ?), \r\n" + 
					"        (SELECT account_id FROM users INNER JOIN accounts ON accounts.user_id = users.user_id WHERE username = ?), ?);";
			
			jdbcTemplate.update(sql, currentTransfer.getAccountFrom(), currentTransfer.getAccountTo(), currentTransfer.getAmount());
			withdrawTEBucks(currentTransfer.getAccountFrom(), currentTransfer.getAmount());
			depositTEBucks(currentTransfer.getAccountTo(), currentTransfer.getAmount());
		}
	}

	@Override
	public void withdrawTEBucks(String usernameFrom, BigDecimal amountToWithdraw) {
		String sql ="UPDATE accounts\r\n" + 
				"SET balance = balance - ?\r\n" + 
				"WHERE account_id = (SELECT account_id FROM users INNER JOIN accounts ON accounts.user_id = users.user_id WHERE username = ?);";

		jdbcTemplate.update(sql, amountToWithdraw, usernameFrom);
		
		
	}

	@Override
	public void depositTEBucks(String usernameTo, BigDecimal amountToDeposit) {
		String sql = "UPDATE accounts\r\n" + 
				"SET balance = balance + ?\r\n" + 
				"WHERE account_id = (SELECT account_id FROM users INNER JOIN accounts ON accounts.user_id = users.user_id WHERE username = ?);";
		
		jdbcTemplate.update(sql, amountToDeposit, usernameTo);
		
	}
	
	@Override
	public Transfer[] getHistory(String username){
		List<Transfer> historyList = new ArrayList<>();
		String sql = 
				"WITH account_name_mapping AS (\r\n" + 
				"    SELECT account_id, username \r\n" + 
				"    FROM accounts \r\n" + 
				"    JOIN users ON accounts.user_id = users.user_id )\r\n" + 
				"\r\n" + 
				"SELECT t.transfer_id, \r\n" + 
				"tt.transfer_type_desc, \r\n" + 
				"ts.transfer_status_desc, \r\n" + 
				"t.amount, \r\n" + 
				"fn.username AS from_name, \r\n" + 
				"tn.username AS to_name\r\n" + 
				"FROM transfers t\r\n" + 
				"INNER JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id\r\n" + 
				"INNER JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id\r\n" + 
				"INNER JOIN account_name_mapping fn ON fn.account_id = t.account_from\r\n" + 
				"INNER JOIN account_name_mapping tn ON tn.account_id = t.account_to\r\n" + 
				"WHERE account_to = (Select user_id FROM users WHERE username = ?) \r\n" + 
				"    OR  account_from = (Select user_id FROM users WHERE username = ?)\r\n" + 
				"ORDER BY t.transfer_id DESC;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);
		
		while(results.next()) {
			historyList.add(mapRowToTransfer(results));
		}
		
		return (Transfer[]) historyList.toArray(new Transfer[historyList.size()]);
	}
	
	@Override
	public Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer foundTransfer = new Transfer();
		foundTransfer.setTransferId(results.getInt("transfer_id"));
		foundTransfer.setTransferType(results.getString("transfer_type_desc"));
		foundTransfer.setTransferStatus(results.getString("transfer_status_desc"));
		foundTransfer.setAmount(results.getBigDecimal("amount"));
		foundTransfer.setAccountFrom(results.getString("from_name"));
		foundTransfer.setAccountTo(results.getString("to_name"));
		
		return foundTransfer;
	}

}
