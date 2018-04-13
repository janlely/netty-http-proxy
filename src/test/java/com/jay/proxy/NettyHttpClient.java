package com.jay.demo.proxy;

import com.sun.deploy.util.SessionState.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.apache.commons.lang3.StringUtils;

public class NettyHttpClient {

    private ChannelFuture future;

    private String host;

    private int port;

    private Bootstrap bootstrap;

    private Channel serverChannel;

    public NettyHttpClient(){
        this.future = null;
        this.host = "";
        this.port = 0;
        bootstrap = null;
    }

    public void connect(String host, int port) throws InterruptedException {
        if(!StringUtils.equals(this.host, host) || this.port != port || future == null){
            this.host = host;
            this.port = port;
            final ClientInboundHandler handler = new ClientInboundHandler(this);
            this.bootstrap = new Bootstrap();
//            this.bootstrap.group(HttpClientPool.getEventLoopGroup())
            this.bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new HttpResponseDecoder());
                        ch.pipeline().addLast(handler);
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .remoteAddress(host, port);
            future = this.bootstrap.connect().sync();
            future.addListener(new ClientConnectionListener(this));
        }
    }

    public void reconnect() throws InterruptedException {
//        future.channel().closeFuture().sync();
        final ClientInboundHandler handler = new ClientInboundHandler(this);
        this.bootstrap = new Bootstrap();
//        this.bootstrap.group(HttpClientPool.getEventLoopGroup())
        this.bootstrap.group(new NioEventLoopGroup(1))
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(handler);
                }
            })
            .option(ChannelOption.SO_KEEPALIVE, true)
            .remoteAddress(host, port);
        future = this.bootstrap.connect().sync();
        future.addListener(new ClientConnectionListener(this));
    }


    public void write(Object ob){
        future.channel().write(ob);
    }

    public void flush(){
        future.channel().flush();
    }

    public String getHost() {
        return host;
    }


    public int getPort() {
        return port;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }
}
