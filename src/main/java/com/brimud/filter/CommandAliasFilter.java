/**
 * 
 */
package com.brimud.filter;

import com.brimud.command.Direction;
import com.brimud.session.Session;
import com.brimud.util.StringUtil;

/**
 * @author dan
 * 
 */
public class CommandAliasFilter implements Filter {

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.filter.Filter#doFilter(com.brimud.session.Session,
   * java.lang.String, com.brimud.filter.FilterChain)
   */
  @Override
  public void doFilter(Session session, String command, FilterChain filterChain) {
    String replacedCommand = replaceAlias(command);
    filterChain.doFilter(session, replacedCommand);
  }
  
  String replaceAlias(String original) {
    String trimmedCommand = StringUtil.trimToNull(original);
    if (trimmedCommand != null) {
      if (trimmedCommand.startsWith("'")) {
        trimmedCommand = "say " + trimmedCommand.substring(1);
      } else if ("nsewud".contains(trimmedCommand.toLowerCase()) && trimmedCommand.length() == 1) {
        trimmedCommand = Direction.fromName(trimmedCommand.toLowerCase()).getName();
      } else if ("l".equals(trimmedCommand)) {
        trimmedCommand = "look";
      }
    }
    return trimmedCommand;
  }

}
