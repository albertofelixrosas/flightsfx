package com.adrianmartinez.flightsfx.utils;

import java.time.LocalTime;
import java.util.List;

public class TimeUtils {

    public static LocalTime timeAverage(List<LocalTime> times) {
        if(times == null || times.size() == 0) {
            return null;
        }
        long nanoSum = 0;
        for (LocalTime duration : times) {
            nanoSum += duration.toNanoOfDay();
        }
        return LocalTime.ofNanoOfDay(nanoSum / (times.size()));
    }

}
