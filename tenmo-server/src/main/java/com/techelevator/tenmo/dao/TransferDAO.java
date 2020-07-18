package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	boolean isTransferAllowed(String usernameFrom, BigDecimal amountToTransfer);
	
	void withdrawTEBucks (String usernameFrom, BigDecimal amountToWithdraw);
	
	void depositTEBucks (String usernameTo, BigDecimal amountToDeposit);
	
	BigDecimal getBalanceByUsername(String username);
	
	void initiateTransfer(Transfer currentTransfer);
	
	Transfer[]getHistory(String username);
	
	Transfer mapRowToTransfer(SqlRowSet results);
	
}
