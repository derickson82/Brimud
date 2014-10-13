/**
 * 
 */
package com.brimud.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dan
 *
 */
public class Events implements EventDispatcher {
  
  private final Map<Class<?>, List<?>> map = new HashMap<Class<?>, List<?>>();
  
  public <L> void listen(Class<? extends Event<L>> clazz, L handler) {
    List<L> listeners = listenersOf(clazz);
    synchronized (listeners) {
      if (!listeners.contains(handler)) {
        listeners.add(handler);
      }
    }
  }
  
  public <L> void mute(Class<? extends Event<L>> clazz, L handler) {
    List<L> listeners = listenersOf(clazz);
    synchronized (listeners) {
     listeners.remove(handler); 
    }
  }
  
  public <L> void fire(Event<L> event) {
    @SuppressWarnings("unchecked")
    Class<? extends Event<L>> clazz = (Class<? extends Event<L>>) event.getClass();
    
    List<L> listeners = listenersOf(clazz);
    for (L listener : listeners) {
      event.notify(listener);
    }
  }
  
  private <L> List<L> listenersOf(Class<? extends Event<L>> clazz) {
    synchronized (map) {
      
      @SuppressWarnings("unchecked")
      List<L> existing = (List<L>) map.get(clazz);
      
      if (existing != null) {
        return existing;
      }
      
      List<L> emptyList = new ArrayList<L>();
      map.put(clazz, emptyList);
      return emptyList;
    }
  }
}
