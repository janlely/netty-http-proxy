package com.jay.demo.proxy;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectionListener implements ChannelFutureListener {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionListener.class);

    private NettyHttpClient client;

    public ClientConnectionListener(NettyHttpClient client){
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(future.isSuccess()){
            logger.info("connected to {}:{}", client.getHost(), client.getPort());
        }
    }
}
