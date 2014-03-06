/**
 * 
 */
package com.brimud.db;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
public class TransactionInterceptor implements MethodInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);
  
  @Inject
  private SessionFactory sessionFactory;

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
   * .MethodInvocation)
   */
  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {

    logger.debug("Entering transaction interceptor");
    
    Transaction transaction = null;
    Session currentSession = null;
    try {
      if (!ManagedSessionContext.hasBind(sessionFactory)) {
        ManagedSessionContext.bind(sessionFactory.openSession());
      }

      currentSession = sessionFactory.getCurrentSession();
      transaction = currentSession.getTransaction();
      
      if (transaction != null && transaction.isActive()) {
        transaction = null;
        currentSession = null;
      } else {
        transaction = currentSession.beginTransaction();
      }

      logger.debug("Invoking proceed() with a transaction started");
      return methodInvocation.proceed();
    } catch (Throwable t) {
      
      if (transaction != null) {
        transaction.rollback();
      }
      
      throw new RuntimeException("General exception caught in the transaction interceptor", t);
    }
    finally {
      
      if (transaction != null && !transaction.wasRolledBack()) {
        logger.debug("Committing transaction");
        transaction.commit();
      }
      if (currentSession != null) {
        currentSession.flush();
        currentSession.close();
      }
      logger.debug("Unbinding the session factory from this context");
      ManagedSessionContext.unbind(sessionFactory);
    }
  }

}
