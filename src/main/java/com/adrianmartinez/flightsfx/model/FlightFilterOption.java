package com.adrianmartinez.flightsfx.model;

public enum FlightFilterOption {

    // Show all flights
    SHOW_ALL("Show all flights"),
    // Show flights to currently selected city
    SHOW_CURRENTLY_CITY("Show flights to currently selected city"),
    //Show long flights
    SHOW_LONG_FLIGHTS("Show long flights"),
    //Show next 5 flights
    SHOW_NEXT_FLIGHTS("Show next 5 flights"),
    // Show flight duration average
    SHOW_FLIGHT_DURATION_AVERAGE("Show flight duration average");

    private final String name;

    FlightFilterOption(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
