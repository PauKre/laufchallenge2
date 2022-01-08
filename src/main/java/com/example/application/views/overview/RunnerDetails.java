package com.example.application.views.overview;

/**
 * Simple DTO class for the inbox list to demonstrate complex object data
 */
public class RunnerDetails {

    private String runnerName;

    private double distance;

    private int numberOfRuns;

    enum Status {
        EXCELLENT, OK, FAILING;
    }

    public RunnerDetails() {

    }

    public RunnerDetails(String runnerName, int distance, int numberOfRuns) {
        this.runnerName = runnerName;
        this.distance = distance;
        this.numberOfRuns = numberOfRuns;
    }

    public String getRunnerName() {
        return runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }
}
