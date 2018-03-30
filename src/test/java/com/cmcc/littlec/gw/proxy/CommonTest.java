package com.cmcc.littlec.gw.proxy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTest {

    private static final Pattern TR69_URL_PATTERN = Pattern.compile(
        "([\\s\\S]*Device\\.ManagementServer\\.ConnectionRequestURL"
            + "[\\s\\S]*)(http://[0-9.:]+/[\\s\\S]+/?)(</Value>[\\s\\S]*)");

    @Test
    public void testReplace() throws IOException {

        String content = IOUtils.toString(new FileInputStream("/home/jay/stb.log"));
        long start = System.currentTimeMillis();
        for(int i = 0; i < 1; i++){
            int idx1 = content.indexOf("Device.ManagementServer.ConnectionRequestURL");
            int idx2 = content.indexOf("http", idx1);
            int idx3 = content.indexOf("<", idx2);
//            System.out.println(content.substring(idx2, idx3));
            content = content.substring(0, idx2) + "http://helloworld:8080/proxy/" + content.substring(idx3);
            System.out.println(content);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
