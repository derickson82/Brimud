/**
 * 
 */
package com.brimud.db;

import javax.inject.Inject;

import com.brimud.account.Account;
import com.brimud.session.Session;
import com.brimud.session.SessionManager;


/**
 * @author dan
 *
 */
public class AccountDao {
	
	private SessionManager manager;
	
	@Inject
	public AccountDao(SessionManager manager) {
		this.manager = manager;
	}

	public Account getById(String accountName) {
		Session session = manager.get(accountName);
		return session != null ? session.getAccount() : null;
	}
	
}
