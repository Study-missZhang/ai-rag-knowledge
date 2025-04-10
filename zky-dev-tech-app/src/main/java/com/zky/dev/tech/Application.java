package com.zky.dev.tech;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: ZhangKaiYuan
 * @description:
 * @create: 2025/4/10
 */
@SpringBootApplication
@Configurable
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
