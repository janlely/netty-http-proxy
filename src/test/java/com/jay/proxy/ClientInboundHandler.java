package com.jay.demo.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.LastHttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientInboundHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ClientInboundHandler.class);

    private NettyHttpClient client;


    public ClientInboundHandler(NettyHttpClient client){
        this.client = client;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        client.getServerChannel().write(msg);
        if(msg instanceof LastHttpContent){
            client.getServerChannel().flush();
            HttpClientPool.giveBack(client);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){

        logger.error("{}: {}", cause.getClass().getName(), cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connection closed, start reconnect to {}:{}", client.getHost(), client.getPort());
        this.client.reconnect();
    }

}
