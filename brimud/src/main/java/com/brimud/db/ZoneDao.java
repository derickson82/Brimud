/**
 * 
 */
package com.brimud.db;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.brimud.model.Zone;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class ZoneDao extends HibernateDao<Zone, String> {

  @Inject
  ZoneDao(SessionFactory sessionFactory) {
    super(Zone.class, String.class, sessionFactory);
  }
  
  /**
   * Assuming there is only one starting zone to begin with
   * @return
   */
  public Zone getStartingZone() {
    return Zone.class.cast(createCriteria().add(Restrictions.eq("startingZone", true)).uniqueResult());
  }

}
