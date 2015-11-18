package com.calamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by dortega on 11/16/15.
 */
@Component
@Scope(value = "prototype")
@ConfigurationProperties(prefix = "device")
public class Device {
    private int id;
    private int typeTemp;
    private Type type;
    private Date createdOn;
    private Date lastMessage;
    private int rpm = 0;
    private int maxRpm = 0;
    private Direction direction;
    private int speed = 0;
    private int maxSpeed = 0;
    private int distance = 0;
    private int totalDistance = 0;
    private double latitude;
    private double longitude;


    @PostConstruct
    public void init() {
        latitude = 33.656634F;
        longitude = -117.754467F;
        type = Type.values()[typeTemp];
        createdOn = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public int getTypeTemp() {
        return typeTemp;
    }

    public void setTypeTemp(int typeTemp) {
        this.typeTemp = typeTemp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public int getMaxRpm() {
        return maxRpm;
    }

    public void setMaxRpm(int maxRpm) {
        maxRpm = maxRpm;
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

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        totalDistance = totalDistance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void update(Message message) {

        this.lastMessage = new Date();
        this.direction = message.getDirection();
        this.maxRpm = (Math.max(this.rpm, message.getRpm()));
        this.rpm = message.getRpm();
        this.maxSpeed = (Math.max(this.maxSpeed, message.getSpeed()));
        this.speed = message.getSpeed();
        this.totalDistance += message.getDistance();
        this.distance = message.getDistance();
        double delta = this.distance;
        switch (this.direction) {

            case N:
                this.latitude += (delta / 6378000) * (180 / Math.PI);
                break;
            case NE:
                delta = Math.sqrt(Math.pow(delta, 2) / 2);
                this.latitude += (delta / 6378000) * (180 / Math.PI);
                this.longitude += ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
            case E:
                this.longitude += ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
            case SE:
                delta = Math.sqrt(Math.pow(delta, 2) / 2);
                this.latitude -= (delta / 6378000) * (180 / Math.PI);
                this.longitude += ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
            case S:
                this.latitude -= (delta / 6378000) * (180 / Math.PI);
                break;
            case SW:
                delta = Math.sqrt(Math.pow(delta, 2) / 2);
                this.latitude -= (delta / 6378000) * (180 / Math.PI);
                this.longitude -= ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
            case W:
                this.longitude -= ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
            case NW:
                this.latitude += (delta / 6378000) * (180 / Math.PI);
                this.longitude -= ((delta / 6378000) * (180 / Math.PI)) / Math.cos(this.latitude * Math.PI / 180);
                break;
        }

    }
}
