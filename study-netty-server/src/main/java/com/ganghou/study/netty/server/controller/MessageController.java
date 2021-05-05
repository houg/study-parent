package com.ganghou.study.netty.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ganghou.study.netty.server.builder.ChannelMapServer;

@RestController
@RequestMapping("/msg")
public class MessageController {
	
	@RequestMapping("/send")
	public boolean send(String id,String obj) {
		return ChannelMapServer.pushNewsById(id, obj);
	}
	
	@RequestMapping("/sendAll")
	public boolean sendAll(String obj) {
		return ChannelMapServer.pushNewsToAllClient(obj);
	}

}
