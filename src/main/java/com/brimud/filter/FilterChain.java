/**
 * 
 */
package com.brimud.filter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.brimud.session.Session;

/**
 * @author dan
 *
 */
public class FilterChain {

  private final List<Filter> filters;
  
  private final ChainTarget target;
  
  private Iterator<Filter> iterator = null;
  
  @Inject
  public FilterChain(ChainTarget target, Filter... filters) {
    this.target = target;
    this.filters = Arrays.asList(filters);
  }
  
  public void filter(Session session, String command) {
    iterator = filters.iterator();
    doFilter(session, command);
    iterator = null;
  }
  
  public void doFilter(Session session, String command) {
    if (iterator == null) {
      throw new IllegalStateException("Must call filter() before calling doFilter");
    }
    if (iterator.hasNext()) {
      Filter filter = iterator.next();
      filter.doFilter(session, command, this);
    } else {
      target.execute(session, command);
      iterator = null;
    }
  }

}
