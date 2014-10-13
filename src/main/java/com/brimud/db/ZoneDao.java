/**
 * 
 */
package com.brimud.db;

import com.brimud.model.Zone;

/**
 * @author dan
 *
 */
public class ZoneDao {

  /**
   * Assuming there is only one starting zone to begin with
   * @return
   */
  public Zone getStartingZone() {
	  throw new RuntimeException("Starting zone has been disabled");
  }

}
