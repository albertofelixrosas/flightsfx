package com.adrianmartinez.flightsfx.utils;

import com.adrianmartinez.flightsfx.global.Formats;
import com.adrianmartinez.flightsfx.model.Flight;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static List<Flight> loadFlights() throws IOException {
        String currentProjectPath = System.getProperty("user.dir");
        String filePath = currentProjectPath + File.separatorChar + "flights.txt";
        Path path = new File(filePath).toPath();
        try (Stream<String> lines = Files.lines(path)){
            List<Flight> result = lines
                    .map((line) -> {
                        // flight_number;destination;departure;duration
                        String[] fields = line.split(";");
                        String flightNumber = fields[0];
                        String destination = fields[1];
                        String departureStr = fields[2];
                        String durationStr = fields[3];
                        LocalDateTime departure = LocalDateTime.parse(departureStr, Formats.dtf);
                        LocalTime duration = LocalTime.parse(durationStr, Formats.tf);
                        Flight flight = new Flight(flightNumber);
                        flight.setDestination(destination);
                        flight.setDeparture(departure);
                        flight.setDuration(duration);
                        return flight;
                    })
                    .collect(Collectors.toList());
            if(result.isEmpty()) {
                return new ArrayList<>();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void saveFlights(List<Flight> flights) throws FileNotFoundException {
        String currentProjectPath = System.getProperty("user.dir");
        String filePath = currentProjectPath + File.separatorChar + "flights.txt";
        try (PrintWriter pw = new PrintWriter(filePath)) {
            flights.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
