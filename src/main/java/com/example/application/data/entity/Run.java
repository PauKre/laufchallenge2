package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.example.application.data.HasKey;
import com.google.firebase.database.IgnoreExtraProperties;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Entity;

@Entity
@IgnoreExtraProperties
public class Run extends AbstractEntity implements HasKey {

    private String key;
    private String name;
    private double distance;
    private double time;
    private Long date;

    public Run() {
    }

    public Run(String name, double distance, double time, LocalDate date) {
        super();
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.date = date.toEpochDay();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double timeInMinutes) {
        this.time = timeInMinutes;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long dateAsLong) {
        this.date = dateAsLong;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Run[name: "+ name + " distance: " + distance + " time: " + time + " date: " + date.toString() + "]";
    }
}
