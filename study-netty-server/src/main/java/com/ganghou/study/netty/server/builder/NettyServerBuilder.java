package com.ganghou.study.netty.server.builder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.ganghou.study.netty.server.handler.MessageHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServerBuilder {

	private int port;

	private static final String zkServers = "172.31.3.31:2181";

	public NettyServerBuilder(int port) {
		super();
		this.port = port;
	}

	public void start() {
		// 负责接收客户端连接
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		// 负责处理客户端读写任务
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap()
				// 线程池组
				.group(bossGroup, workerGroup)
				// 非阻塞通道
				.channel(NioServerSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel channel) throws Exception {

						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast("decoder", new StringDecoder());
						pipeline.addLast("encoder", new StringEncoder());
						pipeline.addLast("messageHandler", new MessageHandler());
						
						InetSocketAddress insocket = (InetSocketAddress) channel.remoteAddress();
						String ip = insocket.getAddress().getHostAddress();

						
					}

				});
		// 绑定端口，同步等待成功
		ChannelFuture future = null;
	
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			registerToNacos(ip);
			System.out.println("netty服务器在" + port + "端口启动监听");
			// 真正让netty跑起来的重点
			future = serverBootstrap.bind(port).sync();
			if (future.isSuccess()) {
				System.out.println("netty服务开启成功");
			} else {
				System.out.println("netty服务开启失败");
			}
			// 等待服务监听端口关闭,就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
			future.channel().closeFuture().addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					deregisterToNacos(ip);
					
				}
				
			}).sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 优雅地退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	public void register() {
		System.out.println("注册服务");
		String path = "/netty";
		// 创建会话
		ZkClient zkClient = new ZkClient(zkServers, 3000);
		zkClient.create(path, "127.0.0.1:" + port, CreateMode.EPHEMERAL);
		// 监听数据
		zkClient.subscribeDataChanges(path, new IZkDataListener() {

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {

			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {

			}
		});

		zkClient.subscribeChildChanges(path, new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

			}

		});
	}
	private NamingService naming = null;
	
	public void registerToNacos(String ip) {
		try {
			naming = NamingFactory.createNamingService("172.31.3.25:8848");
			naming.registerInstance("netty.1", ip, port,"netty");
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deregisterToNacos(String ip) {
		try {
			naming.deregisterInstance("netty.1", ip, port,"netty");
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
