package com.ctts.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liangbaichuan
 */
@SpringBootApplication
public class CttsUserApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CttsUserApplication.class);


    public static void main(String[] args) {
        try {
            SpringApplication.run(CttsUserApplication.class,args);
        }catch (Exception e){
            LOG.error("启动项错误信息：{}", e.getMessage());
        }
    }
}
