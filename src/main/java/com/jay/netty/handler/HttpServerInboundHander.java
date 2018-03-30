package com.jay.netty.handler;

import com.jay.netty.client.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerInboundHander extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHander.class);

    private String host;

    private int port;

    public HttpServerInboundHander(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            NettyHttpClient client = new NettyHttpClient(ctx.channel().eventLoop().parent(), ctx);
            client.create();
            client.sendRequest(request, host, port);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("HttpServerInboundHandler.channelReadComplete");
        ctx.flush();
    }
}
