package com.brimud.netty;

import javax.inject.Inject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.session.Session;
import com.brimud.session.SessionListener;
import com.brimud.session.SessionManager;

public class BrimudServerHandler extends SimpleChannelInboundHandler<String> {
	
	private static final Logger logger = LoggerFactory.getLogger(BrimudServerHandler.class);
	
	private SessionManager sessionManager;
	private Session session;

	@Inject
	public BrimudServerHandler(SessionManager sessionManager, Session session) {
		this.sessionManager = sessionManager;
		this.session = session;
	}
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		logger.info("New channel activated");
		
		session.addSessionListener(new SessionListener() {
			
			@Override
			public void init(Session session) {
				session.checkLoginState(null);
			}
			
			@Override
			public void message(Session session, String message) {
				ctx.writeAndFlush(message);
			}
			
			@Override
			public void quit(Session session, String message) {
				try {
					ctx.writeAndFlush(message).await();
				} catch (InterruptedException e) {
					logger.error("There was a problem writing the quit message");
				}
				ctx.close();
			}
			
			@Override
			public void closing(Session session) {
				sessionManager.closing(session);
			}
		});
		
		session.fireConnect();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
		logger.info("Received message: {}", message);
		session.fireCommand(message);
	}
	
}
