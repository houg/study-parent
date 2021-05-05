package com.ganghou.study.netty.server.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelMapServer {
	
	private final static Map<String,Channel> channelMap =  new ConcurrentHashMap<String,Channel>();

    /**
     *  获取ConcurrentHashMap
     */
    public static Map<String, Channel> getChannelHashMap() {
        return channelMap;
    }

    /**
     *  获取指定id的channel
     */
    public static Channel getChannelById(String id){
        if(channelMap==null||channelMap.isEmpty()){
            return null;
        }
        return channelMap.get(id);
    }

    /**
     *  将通道中的消息推送到每一个客户端
     */
    public static boolean pushNewsToAllClient(String obj){
        if(obj==null||channelMap.isEmpty()){
            return false;
        }
        for(String id: channelMap.keySet()) {
            Channel channel = channelMap.get(id);
            channel.writeAndFlush(obj);
        }
        return true;
    }
    
    /**
     *  将通道中的消息推送到每一个客户端
     */
    public static boolean pushNewsById(String id,String obj){
        if(obj==null||channelMap.isEmpty()||!channelMap.containsKey(id)){
            return false;
        }
        Channel channel = channelMap.get(id);
        channel.writeAndFlush(obj);
        return true;
    }

    /**
     *  将channel和对应的name添加到ConcurrentHashMap
     */
    public static void addChannel(String id,Channel channel){
        
    	channelMap.put(id,channel);
      
    }

    /**
     *  移除掉name对应的channel
     */
    public static boolean removeChannelById(String id){
        if(channelMap.containsKey(id)){
        	channelMap.remove(id);
        }
        return true;
    }


}
