package com.jay.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter{

    private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHander.class);

    private ChannelHandlerContext channelCtx;

    public HttpClientInboundHandler(ChannelHandlerContext channelCtx){
        this.channelCtx = channelCtx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        HttpResponse response = (HttpResponse) msg;
        this.channelCtx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("HttpClient.channelReadComplete");
        ctx.flush();
    }
}
