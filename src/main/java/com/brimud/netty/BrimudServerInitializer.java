package com.brimud.netty;

import javax.inject.Inject;
import javax.inject.Provider;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class BrimudServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	
	private static final int MAX_FRAME_LENGTH = 8192;

	private final Provider<BrimudServerHandler> handlerProvider;
	
	@Inject
	public BrimudServerInitializer(Provider<BrimudServerHandler> handlerProvider) {
		this.handlerProvider = handlerProvider;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, Delimiters.lineDelimiter()));
		
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);
		
		pipeline.addLast(handlerProvider.get());
	}
}
