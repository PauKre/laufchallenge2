package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Run extends AbstractEntity {

    private String name;
    private double distance;
    private double time;
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
