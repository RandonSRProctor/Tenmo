package com.techelevator.tenmo.services;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;

public class TransfersService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public TransfersService (String url) {
		this.BASE_URL = url;
	}
//	
//	public void sendTransfer (Transfer transfer, String token) {
//		//how Do we serialize JSON data so that we can send to the controller?
//	}
	
	public void sendTransfer(Transfer transfer, String token) {
		try {	
			restTemplate.exchange(BASE_URL + "sendtransfer", HttpMethod.POST, makeTransferEntity(transfer, token), Transfer.class);
			
		} catch(RestClientResponseException ex) {
			String message = ex.getMessage();
			System.out.println(message);
        }
	}
	
	public Transfer[] history(String token){
		try {
			//BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class
			Transfer[] transferHistory = restTemplate.exchange(BASE_URL + "history", HttpMethod.GET, makeAuthEntity(token), Transfer[].class).getBody();
			return transferHistory;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
	
    @SuppressWarnings("rawtypes")
	private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
