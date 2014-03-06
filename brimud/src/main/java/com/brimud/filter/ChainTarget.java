/**
 * 
 */
package com.brimud.filter;

import com.brimud.session.Session;

/**
 * @author dan
 *
 */
public interface ChainTarget {
  void execute(Session session, String command);
}
