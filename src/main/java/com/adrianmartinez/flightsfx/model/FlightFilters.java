package com.adrianmartinez.flightsfx.model;

import com.adrianmartinez.flightsfx.utils.TimeUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightFilters {

    public static List<Flight> showAll(List<Flight> flights) {
        return flights;
    }
    // SHOW_CURRENTLY_CITY
    public static List<Flight> showCurrentlyCity(List<Flight> flights, String city) {
        return flights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    // Show next 5 flights
    public static List<Flight> showNextFlights(List<Flight> flights) {
        LocalDateTime todayNow = LocalDateTime.now();
        return flights.stream()
                .filter(flight -> flight.getDeparture().compareTo(todayNow) >= 0)
                .limit(5)
                .collect(Collectors.toList());
    }

    public static LocalTime showFlightsAverageDuration(List<Flight> flights) {
        List<LocalTime> durations = flights.stream()
                .map(Flight::getDuration)
                .collect(Collectors.toList());
        LocalTime average = TimeUtils.timeAverage(durations);
        return average;
    }

    public static List<Flight> showLongFlights(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getDuration().getHour() > 3)
                .collect(Collectors.toList());
    }

}
