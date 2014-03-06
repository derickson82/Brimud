/**
 * 
 */
package com.brimud.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.ManagedSessionContext;

import com.brimud.account.Account;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.Zone;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;

/**
 * @author dan
 *
 */
public class DatabaseModule extends AbstractModule {

  private static final String DB_CONNECTION_STRING = "db.connection.string";
  private static final String DB_USERNAME = "db.username";
  private static final String DB_PASSWORD = "db.password";

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    TransactionInterceptor txInterceptor = new TransactionInterceptor();
    requestInjection(txInterceptor);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transacted.class), txInterceptor);
  }
  
  @Singleton
  @Provides
  public SessionFactory provideSessionFactory(@Named(DB_CONNECTION_STRING) String connectionString,
      @Named(DB_USERNAME) String userName, @Named(DB_PASSWORD) String password) {
    Configuration cfg = new Configuration();
    cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
    cfg.setProperty("hibernate.connection.url", connectionString);
    cfg.setProperty("hibernate.connection.username", userName);
    cfg.setProperty("hibernate.connection.password", password);

    cfg.setProperty("hibernate.connection.pool_size", "10");
    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    cfg.setProperty("hibernate.current_session_context_class", "managed");
    cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
    cfg.setProperty("hibernate.show_sql", "true");
    
    cfg.setProperty("hibernate.c3p0.acquire_increment", "3");
    cfg.setProperty("hibernate.c3p0.idle_test_period", "14400"); // test every 4 hours
    cfg.setProperty("hibernate.c3p0.timeout", "25200");
    cfg.setProperty("hibernate.c3p0.max_size", "15");
    cfg.setProperty("hibernate.c3p0.min_size", "3");
    cfg.setProperty("hibernate.c3p0.max_statements", "0");
    cfg.setProperty("hibernate.c3p0.preferredTestQuery", "select 1;");

    cfg.addClass(Account.class);
    cfg.addClass(Player.class);
    cfg.addClass(Room.class);
    cfg.addClass(Zone.class);
    cfg.addClass(com.brimud.character.Character.class);
    SessionFactory sessionFactory = cfg.buildSessionFactory();
    return sessionFactory;
  }

  @Provides
  public org.hibernate.Session provideHibernateSession(SessionFactory sessionFactory) {
    final org.hibernate.Session session;
    if (ManagedSessionContext.hasBind(sessionFactory)) {
      session = sessionFactory.getCurrentSession();
    } else {
      session = sessionFactory.openSession();
      ManagedSessionContext.bind((org.hibernate.classic.Session) session);
    }
    return sessionFactory.getCurrentSession();
  }



}
