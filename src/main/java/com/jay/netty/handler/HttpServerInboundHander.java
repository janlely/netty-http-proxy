package com.jay.netty.handler;

import com.jay.netty.client.NettyHttpClient;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.Default;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.invoke.MethodHandle;
import java.net.URISyntaxException;

public class HttpServerInboundHander extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpServerInboundHander.class);

    private String host;

    private int port;

    private HttpMethod method;

    private String uri;

    private ByteBuf buf;

    private HttpHeaders headers;

    public HttpServerInboundHander(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException, URISyntaxException {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest) msg;
            method = request.method();
            uri = request.uri();
            headers = request.headers();
        }else if(msg instanceof LastHttpContent){
            HttpContent content = (HttpContent) msg;
            buf = content.content();
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, method, uri, buf, headers, new DefaultHttpHeaders());
            NettyHttpClient client = new NettyHttpClient(ctx.channel().eventLoop().parent());
            client.create(new HttpClientInboundHandler(ctx));
            client.sendRequest(request, host, port);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("HttpServerInboundHandler.channelReadComplete");
        ctx.flush();
    }
}
