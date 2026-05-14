package com.palestra.palestra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PalestraApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalestraApplication.class, args);
    }

}
