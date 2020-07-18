package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountsService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransfersService;
import com.techelevator.tenmo.services.UsersService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String TRANSFER_MENU_SEE_ALL_TRANSACTIONS = "View full transaction history";
	private static final String TRANSFER_MENU_SELECT_TRANSACTION_BY_ID = "View transaction by specific ID";
	private static final String TRANSFER_MENU_RETURN_TO_PREVIOUS_MENU = "Return to previous menu";
	private static final String[] TRANFSER_MENU_OPTIONS  = {TRANSFER_MENU_SEE_ALL_TRANSACTIONS, TRANSFER_MENU_SELECT_TRANSACTION_BY_ID, TRANSFER_MENU_RETURN_TO_PREVIOUS_MENU};
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountsService accountsService;
    private UsersService usersService;
    private TransfersService transfersService;
   

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountsService(API_BASE_URL), new UsersService(API_BASE_URL), new TransfersService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountsService accountsService, UsersService usersService, TransfersService transfersService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountsService = accountsService;
		this.usersService = usersService;
		this.transfersService = transfersService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		console.displayMessage("Account balance: " + accountsService.getBalance(currentUser.getToken()) + " TE bucks.");
	}

	private void viewTransferHistory() {
		
		String choice = (String)console.getChoiceFromOptions(TRANFSER_MENU_OPTIONS);
		
		if (TRANSFER_MENU_SEE_ALL_TRANSACTIONS.equals(choice)) {
			
			Transfer[] history = transfersService.history(currentUser.getToken());
			
			for(Transfer t: history) {
				console.displayMessage(t.toString());
			}
			
		} else if (TRANSFER_MENU_SELECT_TRANSACTION_BY_ID.equals(choice)) {
			
			int transferIdSearchQuery = console.getUserInputInteger("Please enter transfer ID number");
			
			Transfer[] history = transfersService.history(currentUser.getToken());
			
			boolean found = false;
			
			for(Transfer t: history) {
				if (t.getTransferId() == transferIdSearchQuery) {
					console.displayMessage("\n" + t.toString());
					found = true;
				}
			
			}
			
			if (!found) {
				console.displayMessage("Sorry, that ID does appear in your transaction history");
			}
			
		} else if (TRANSFER_MENU_RETURN_TO_PREVIOUS_MENU.equals(choice)) {
			
		}
			
	
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		//load in list of users from API
		// Remove current user from list of options to transfer to
		// Allow user to select whom they want to transfer money to
		List<String> usernames = usersService.getUsernames(currentUser.getToken());
		usernames.remove(currentUser.getUser().getUsername());
		String selectedUser = (String)console.getChoiceFromOptions(usernames.toArray());

		BigDecimal amountToSend = console.getUserInputBigDecimal("Enter amount to send " + selectedUser + ": ");
		String currentUserName = currentUser.getUser().getUsername();
		Transfer transfer = new Transfer(currentUserName, selectedUser, amountToSend);
		transfersService.sendTransfer(transfer, currentUser.getToken());
		// send transfer to transfer service, along with token
		console.displayMessage("Processing transfer... Sending " + amountToSend + " to " + selectedUser);
		viewCurrentBalance();

	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		console.displayMessage("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	console.displayMessage("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	console.displayMessage("REGISTRATION ERROR: "+e.getMessage());
            	console.displayMessage("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				console.displayMessage("LOGIN ERROR: "+e.getMessage());
				console.displayMessage("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
