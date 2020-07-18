package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class UsersService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public UsersService (String url) {
		this.BASE_URL = url;
	}
	
	public List<String> getUsernames (String token) {
		List<String> usernames = new ArrayList<>();
		try {
			usernames = restTemplate.exchange(BASE_URL + "usernames",  HttpMethod.GET, makeAuthEntity(token), List.class).getBody();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return usernames;
	}
	
	private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
