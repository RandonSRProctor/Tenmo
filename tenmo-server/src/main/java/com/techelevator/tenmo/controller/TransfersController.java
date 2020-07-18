package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransfersController {

	private TransferDAO transferDAO;
	
	public TransfersController (TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}
	
	
	@RequestMapping (value = "/sendtransfer", method = RequestMethod.POST)
	public void sendTransfer (@RequestBody  @Valid Transfer currentTransfer) {
		transferDAO.initiateTransfer(currentTransfer);
	}
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public Transfer[] getTransferHistory(Principal principal){
		Transfer[] history = transferDAO.getHistory(principal.getName());
		
		
		return history;
	}

}
