package com.adrianmartinez.flightsfx.model;

import com.adrianmartinez.flightsfx.global.Formats;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight {

    private String flightNumber;
    private String destination;
    private LocalDateTime departure;
    private LocalTime duration;

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Flight(String flightNumber, String destination, LocalDateTime departure, LocalTime duration) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departure = departure;
        this.duration = duration;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public String getDepartureWithDateFormat() {
        return departure.format(Formats.dtf);
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(flightNumber).append(';')
                .append(destination).append(';')
                .append(departure.format(Formats.dtf)).append(';')
                .append(duration.format(Formats.tf))
                .toString();
    }

}
