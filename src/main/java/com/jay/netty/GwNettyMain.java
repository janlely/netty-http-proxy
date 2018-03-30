/*
 * Project: littlec-rms-proxy
 *
 * File Created at Fri, 09 Feb 2018 16:27:46
 *
 * Copyright 2018 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.jay.netty;

import com.jay.netty.server.BootStrapServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Type EmsManager
 * @Desc
 * @author jinjunjie
 * @date Tue, 03 Jan 2017 16:27:46, 2017
 * @version
 */
public class GwNettyMain {

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        BootStrapServer bootStrapServer = (BootStrapServer) context.getBean("bootStrapServer");
        bootStrapServer.start(8000);

    }
}

/**
 * Revision history
 * -------------------------------------------------------------------------
 *
 * Date Author Note
 * -------------------------------------------------------------------------
 * Fri, 09 Feb 2018 16:27:46 jinjunjie create
 */
