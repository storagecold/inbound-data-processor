package com.cold.storage.inbound.data.processor.processor;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

public class Processor {

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    private void run() {
        File stopFile =  new File()
    }
}
