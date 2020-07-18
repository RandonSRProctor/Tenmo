package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDAO;

@RestController
@PreAuthorize("isAuthenticated()")
public class UsersController {
	private UserDAO userDAO;
	
	public UsersController (UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	
	@RequestMapping(value = "/usernames", method = RequestMethod.GET)
	public List<String> getAllUsernames() {
		return userDAO.findAllUsernames();
	}

}
