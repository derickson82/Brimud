/**String playerName
 * 
 */
package com.brimud.filter;

import com.brimud.session.Session;


/**
 * @author dan
 *
 */
public interface Filter {
  void doFilter(Session session, String command, FilterChain filterChain);
}
