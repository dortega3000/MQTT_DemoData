package com.calamp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Time;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class MqttApplication {


    public static void main(String[] args) {
        SpringApplication.run(MqttApplication.class, args);
    }

    @Bean
    public ObjectMapper jacksonMapper() {
        return new ObjectMapper();
    }
}
