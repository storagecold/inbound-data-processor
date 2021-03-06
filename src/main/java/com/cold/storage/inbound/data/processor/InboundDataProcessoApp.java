package com.cold.storage.inbound.data.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InboundDataProcessoApp {
    public static void main(String[] args) {
        SpringApplication.run(InboundDataProcessoApp.class, args);
    }
}
