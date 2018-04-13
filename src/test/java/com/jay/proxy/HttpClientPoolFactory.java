package com.jay.demo.proxy;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientPoolFactory extends BasePooledObjectFactory<NettyHttpClient> {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientPoolFactory.class);

    @Override
    public NettyHttpClient create() throws Exception {
        logger.info("NettyHttpClient been created");
        return new NettyHttpClient();
    }

    @Override
    public PooledObject<NettyHttpClient> wrap(NettyHttpClient nettyHttpClient) {
        return new DefaultPooledObject<>(nettyHttpClient);
    }

}
