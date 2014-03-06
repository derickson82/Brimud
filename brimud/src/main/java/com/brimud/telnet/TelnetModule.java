/**
 * 
 */
package com.brimud.telnet;

import com.google.inject.AbstractModule;

/**
 * @author dan
 *
 */
public class TelnetModule extends AbstractModule {

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    requestStaticInjection(MudShell.class);
  }

}
