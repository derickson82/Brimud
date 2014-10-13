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
		
	void closing(Session session);
}
