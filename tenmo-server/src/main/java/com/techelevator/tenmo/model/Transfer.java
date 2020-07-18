package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Transfer {
	
	private int transferId;
	private String transferType;
	private String transferStatus;
	@NotNull (message = "The accountFrom field cannot be null")
	@NotBlank (message = "The accountFrom field cannot be blank")
	private String accountFrom;
	@NotNull (message = "The accountTo field cannot be null")
	@NotBlank (message = "The accountTo field cannot be blank")
	private String accountTo;
	@DecimalMin (value = "0.0", message = "The amount field must be larger than zero")
	private BigDecimal amount;
	
	public Transfer() {
		
	}
	
	public Transfer(String accountFrom, String accountTo, BigDecimal amount) {
		this.transferType = "Send";
		this.transferStatus = "Approved";
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
		
	}
	
	public Transfer(String transferType, String transferStatus, String accountFrom, String accountTo, BigDecimal amount) {
		this.transferType = transferType;
		this.transferStatus = transferStatus;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String toString() {
		String result = 
				"Transfer ID: " + transferId + "\n" +
				"Transfer Type: " + transferType + "\n" +
				"Transfer Status: " + transferStatus + "\n" +
				"To: " + accountTo + "\n" +
				"From: " + accountFrom + "\n" +
				"Amount: " + amount.toString();
		
		return result;
	}
	

}
