package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AccountsService {
	

	private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountsService(String url) {
        this.BASE_URL = url;
    }
    
    public BigDecimal getBalance(String token) {
    	BigDecimal balance = BigDecimal.ZERO;
    	try {
    		balance = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(token), BigDecimal.class).getBody();
    	} catch (Exception e) {
    		System.out.print(e.getMessage());
    	}
    	
    	return balance;
    }
    
    // Method to receive transfer data, call API to pass off transfer data (Format: JSON or transfer object?)
    
    
    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
