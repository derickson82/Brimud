/**
 * 
 */
package com.brimud.filter;

import com.brimud.session.Session;

/**
 * @author dan
 *
 */
public class SessionChainTarget implements ChainTarget {

  /* (non-Javadoc)
   * @see com.brimud.filter.ChainTarget#execute(com.brimud.session.Session, java.lang.String)
   */
  @Override
  public void execute(Session session, String message) {
    session.addMessage(message);
  }

}
