package com.brimud.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrimudServer {
	
	private static final Logger logger = LoggerFactory.getLogger(BrimudServer.class);
	
	private final int port;
	
	private final EventLoopGroup bossGroup;
	
	private final EventLoopGroup workerGroup;
	
	private final BrimudServerInitializer initializer;
	
	@Inject
	public BrimudServer(@Named("brimud.server.port") int port, BrimudServerInitializer initializer) {
		this.port = port;
		this.initializer = initializer;
		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();
	}
	
	public void run() throws Exception {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(initializer)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			bootstrap.bind(port).sync().channel().closeFuture().sync();
		} finally {
			logger.info("Shutting down event loop groups");
			shutdown();
		}
	}
	
	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}
	
}
