package com.jay.netty.client;

import com.jay.netty.handler.HttpClientInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyHttpClient {

    private EventLoopGroup workerGroup;

    private ChannelHandlerContext ctx;
    private Bootstrap bootstrap;
    public NettyHttpClient(EventLoopGroup workerGroup, ChannelHandlerContext ctx){
        this.workerGroup = workerGroup;
        this.ctx = ctx;
    }

    public void create(){
        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpResponseEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler(ctx));
                }
            });
    }

    public void sendRequest(final HttpRequest request, String host, int port) throws InterruptedException {
        ChannelFuture f = this.bootstrap.connect(host, port).sync();
        f.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    future.channel().write(request);
                    future.channel().flush();
                }
            }
        });
    }
}
