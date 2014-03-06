/**
 * 
 */
package com.brimud.db;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.filter.Filter;
import com.brimud.filter.FilterChain;
import com.brimud.service.MessageService;
import com.brimud.session.Session;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class HibernateFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(HibernateFilter.class);
  
  private final MessageService messageService;
  private final SessionFactory sessFactory;
  
  @Inject
  HibernateFilter(SessionFactory sessFactory, MessageService messageService) {
    this.sessFactory = sessFactory;
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.session.Filter#doFilter(java.lang.String)
   */
  @Override
  public void doFilter(Session session, String str, FilterChain filterChain) {
    
    Transaction transaction = null;
    try {
      if (!ManagedSessionContext.hasBind(sessFactory)) {
        ManagedSessionContext.bind(sessFactory.openSession());
      }
  
      org.hibernate.classic.Session currentSession = sessFactory.getCurrentSession();
      transaction = currentSession.beginTransaction();
      
      filterChain.doFilter(session, str);
      
      transaction.commit();
    } catch (HibernateException e) {
      logger.error("Hibernate exception. If a transaction was in progress, an attempt will be made to roll it back", e);
      if (transaction != null) {
        try {
          transaction.rollback();
        } catch (HibernateException rollbackEx) {
          logger.error("Could not roll back transaction!", rollbackEx);
        }
      }
      messageService.sendMessage(session.getAccount().getPlayer().getName(), "[System] Internal error. Please notify a mud admin of this problem.");
    } catch (Exception e) {
      logger.error("Some other error occurred", e);
    } finally {
      ManagedSessionContext.unbind(sessFactory);
    }
  }

}
