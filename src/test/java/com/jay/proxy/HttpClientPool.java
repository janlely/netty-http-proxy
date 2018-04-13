package com.jay.demo.proxy;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientPool {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientPool.class);

    private static GenericObjectPool<NettyHttpClient> pool;

    private static NioEventLoopGroup eventLoopGroup;

    private static int POOL_SIZE = NettyRuntime.availableProcessors() * 2;

    public static void init(){
        logger.info("HttpClientPool initialized");
        pool = new GenericObjectPool<>(new HttpClientPoolFactory());
        pool.setMaxTotal(POOL_SIZE);
//        eventLoopGroup = new NioEventLoopGroup(POOL_SIZE);
    }

    public static NettyHttpClient borrow() throws Exception {
        logger.info("number of active object: {}, pool size: {}, number of waiters: {}, number of created: {}",
            pool.getNumIdle(), pool.getMaxTotal(), pool.getNumWaiters(), pool.getCreatedCount());
        return pool.borrowObject();
    }

    public static void giveBack(NettyHttpClient ob){
        pool.returnObject(ob);
        logger.info("object returned, number of active object: {}", pool.getNumIdle());
    }

    public static NioEventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }
}
