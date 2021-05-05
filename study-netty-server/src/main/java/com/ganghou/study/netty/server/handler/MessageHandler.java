package com.ganghou.study.netty.server.handler;

import com.ganghou.study.netty.server.builder.ChannelMapServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

public class MessageHandler  implements ChannelInboundHandler{

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
	       System.out.println("handlerAdded.......");
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerRemoved.......");
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelRegistered.......");
		
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelUnregistered.......");
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String id = ctx.channel().id().asShortText();
		ChannelMapServer.addChannel(id, ctx.channel());
        ctx.writeAndFlush("客户端["+id+"]连接成功");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		String id = ctx.channel().id().asShortText();
        ChannelMapServer.removeChannelById(id);
        System.out.println("netty客户端与服务端连接关闭..."+id);

		
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 System.out.println("服务器端收到消息" + msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		
	}


	

}
