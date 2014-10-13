/**
 * 
 */
package com.brimud.event;

/**
 * @author dan
 *
 */
public interface EventDispatcher {
  <L> void fire(Event<L> event);
}
