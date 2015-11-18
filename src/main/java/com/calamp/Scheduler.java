package com.calamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by dortega on 11/5/15.
 */
@Component
public class Scheduler {

    @Autowired
    private MQTTClient mqttClient;

    @Scheduled(fixedDelayString = "${scheduler.interval:100}")
    public void run() throws Exception {
        mqttClient.sendMessage();
    }
}
