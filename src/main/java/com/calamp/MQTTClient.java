package com.calamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dortega on 11/5/15.
 */

@Component
@ConfigurationProperties(prefix = "broker")
public class MQTTClient {


    private String topicTemplate = "/1/{0,number,#}/raw";
    private int qos = 2;
    private String url = "tcp://127.0.0.1:1883";
    private String clientId = "JavaSample";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    MqttClient mqttClient;
    Map<Integer, Device> devices = new HashMap<>();


    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MQTTClient.class);

    @PostConstruct
    public void init() {
        Assert.notNull(applicationContext);
        Assert.notNull(objectMapper);

        while (devices.size() < 5) {
            Device device = applicationContext.getBean(Device.class);
            if (!devices.containsKey(device.getId())) {
                devices.put(device.getId(), device);
            }
        }
        try {
            mqttClient = new MqttClient(url, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            logger.info("Connecting to broker: " + url);
            mqttClient.connect(connOpts);
            logger.info("Connected");
        } catch (MqttException e) {
            logger.debug("Error connecting to MQTT gateway", e);
        }

    }

    @PreDestroy
    public void destroy() {
        try {
            mqttClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException e) {
            logger.debug("Error disconnecting from MQTT gateway", e);
        }
    }


    public void sendMessage() throws Exception {

        try {
            Message message = applicationContext.getBean(Message.class);
            Device device=devices.get(message.getDeviceId());
            device.update(message);
            String content = objectMapper.writeValueAsString(device);
            MqttMessage payload = new MqttMessage(content.getBytes());
            payload.setQos(qos);
            String topic = MessageFormat.format(topicTemplate, device.getId());
            mqttClient.publish(topic, payload);
            logger.info("Publishing topic [" + topic + "] message: " + content + "");
        } catch (MqttException e) {
            logger.debug("Error Sending MQTT Message", e);
        } catch (JsonProcessingException e) {
            logger.debug("Error Converting to JSON", e);
        }
    }


    public String getTopicTemplate() {
        return topicTemplate;
    }

    public void setTopicTemplate(String topic) {
        this.topicTemplate = topic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
