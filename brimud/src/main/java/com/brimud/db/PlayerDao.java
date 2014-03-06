/**
 * 
 */
package com.brimud.db;

import org.hibernate.SessionFactory;

import com.brimud.model.Player;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class PlayerDao extends HibernateDao<Player, String> {

  @Inject
  PlayerDao(SessionFactory sessionFactory) {
    super(Player.class, String.class, sessionFactory);
  }
  
}
