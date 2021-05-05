package com.ganghou.study.netty.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ganghou.study.netty.server.builder.NettyServerBuilder;

@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner{
	
	public static void main(String[] args) {
		
		SpringApplication.run(NettyServerApplication.class, args);
		
	}
     
	/*
	 * 加载完成
	 */
	@Override
	public void run(String... args) throws Exception {
		NettyServerBuilder builder = new NettyServerBuilder(9999);
		builder.start();
		
	}
}
