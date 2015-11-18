package com.calamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by dortega on 11/5/15.
 */
@Component
@Scope(value = "prototype")
@ConfigurationProperties(prefix = "message")
public class Message {


    private int deviceId;
    private int rpm;
    private int directionTemp;
    private Direction direction;
    private int speed;
    private int distance;

    @PostConstruct
    public void init() {
        direction = Direction.values()[directionTemp];
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getDirectionTemp() {
        return directionTemp;
    }

    public void setDirectionTemp(int directionTemp) {
        this.directionTemp = directionTemp;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
