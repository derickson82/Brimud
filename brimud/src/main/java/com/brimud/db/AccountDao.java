/**
 * 
 */
package com.brimud.db;

import org.hibernate.SessionFactory;

import com.brimud.account.Account;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class AccountDao extends HibernateDao<Account, String> {

  @Inject
  AccountDao(SessionFactory sessionFactory) {
    super(Account.class, String.class, sessionFactory);
  }
}
