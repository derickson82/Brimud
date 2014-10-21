/**
 * 
 */
package com.brimud.session;


/**
 * @author dan
 *
 */
public interface SessionListener {
	
	void init(Session session);
	
	void message(Session session, String message);
	
	void quit(Session session, String message);
		
	void closing(Session session);
}
