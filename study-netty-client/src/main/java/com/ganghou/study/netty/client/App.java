package com.ganghou.study.netty.client;

import com.ganghou.study.netty.client.handler.MessageHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		String ip = "127.0.0.1";
		int port = 9999;
		
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		
	    Bootstrap bootstrap = new Bootstrap().group(bossGroup)
	    		.channel(NioSocketChannel.class)
	            .option(ChannelOption.SO_KEEPALIVE, true)
	            .handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast("decoder", new StringDecoder());
			            pipeline.addLast("encoder", new StringEncoder());
						pipeline.addLast("messageHandler",new MessageHandler());
						
					}});
	  // 客户端开启
      ChannelFuture cf = bootstrap.connect(ip, port).sync();
      cf.channel().closeFuture().sync();   
	}
}
