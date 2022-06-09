package com.adrianmartinez.flightsfx.global;

import java.time.format.DateTimeFormatter;

public class Formats {

    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

}
