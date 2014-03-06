/**
 * 
 */
package com.brimud.filter;

import com.brimud.session.Session;

/**
 * @author dan
 *
 */
public class StateMachineFilter implements Filter {

  /* (non-Javadoc)
   * @see com.brimud.filter.Filter#doFilter(com.brimud.session.Session, java.lang.String, com.brimud.filter.FilterChain)
   */
  @Override
  public void doFilter(Session session, String command, FilterChain filterChain) {
    if (session.checkLoginState(command)) {
      filterChain.doFilter(session, command);
    }
  }

}
