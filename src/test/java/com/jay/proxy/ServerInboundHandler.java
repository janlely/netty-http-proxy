package com.jay.demo.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerInboundHandler.class);

    private NettyHttpClient client;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof HttpRequest){
            client = HttpClientPool.borrow();
            client.connect("127.0.0.1", 8080);
            client.setServerChannel(ctx.channel());
        }
        client.write(msg);
        if(msg instanceof LastHttpContent){
            client.flush();
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
}
