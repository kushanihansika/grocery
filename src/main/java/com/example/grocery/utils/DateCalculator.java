package com.example.grocery.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateCalculator {


    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        // Including both the start date and end date
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
