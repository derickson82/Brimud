/**
 * 
 */
package com.brimud.filter;

import com.brimud.command.CommandInterpretter;
import com.brimud.statemachine.StateMachine;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

/**
 * @author dan
 *
 */
public class FilterModule extends AbstractModule {

  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
  }

  @Provides
  @CommandFilterChain
  public FilterChain commandFilterChain(CommandAliasFilter commandAliasFilter, PromptFilter promptFilter, StateMachineFilter stateMachineFilter, CommandInterpretter commandInterpretter) {
    return new FilterChain(commandInterpretter, promptFilter, stateMachineFilter, commandAliasFilter);
  }

  @Provides
  @MessageFilterChain
  public FilterChain commandFilterChain(SessionChainTarget sessionTarget) {
    return new FilterChain(sessionTarget);
  }
  
  @Provides
  @Named("loginSm")
  public StateMachine loginStateMachine(LoginAndRegistration login) {
    return new StateMachine(login);
  }

}
