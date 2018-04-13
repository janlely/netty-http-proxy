package com.jay.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter{

    private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHander.class);

    private ChannelHandlerContext channelCtx;

    private HttpHeaders headers;

    private ByteBuf buf;

    private HttpResponseStatus status;

    public HttpClientInboundHandler(ChannelHandlerContext channelCtx){
        this.channelCtx = channelCtx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof HttpResponse){
            HttpResponse proxyResponse = (HttpResponse) msg;
            headers = proxyResponse.headers();
            status = proxyResponse.status();
        }else if(msg instanceof HttpContent){
            HttpContent response = (HttpContent) msg;
            buf = response.content();
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf, headers, new DefaultHttpHeaders()) ;
            this.channelCtx.writeAndFlush(fullHttpResponse);
            this.channelCtx.close();
            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("HttpClient.channelReadComplete");
        ctx.flush();
    }
}
