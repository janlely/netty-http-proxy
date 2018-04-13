package com.jay.demo.proxy;

public class TestHttpClient {

    public static void main(String[] args) throws Exception {
        HttpClientPool.init();
        NettyHttpClient client = HttpClientPool.borrow();
    }
}
