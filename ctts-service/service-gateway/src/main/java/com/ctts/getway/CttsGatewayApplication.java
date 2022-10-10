package com.ctts.getway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liangbaichuan
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CttsGatewayApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CttsGatewayApplication.class);


    public static void main(String[] args) {
        try {
            SpringApplication.run(CttsGatewayApplication.class,args);
        }catch (Exception e){
            LOG.error("启动项错误信息：{}", e.getMessage());
        }
    }
}
