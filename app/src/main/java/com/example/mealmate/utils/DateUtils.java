package com.example.mealmate.utils;

import java.util.ArrayList;
import java.util.Calendar;

public class DateUtils {

    // Return the current day of the week as a string (e.g., "Monday")
    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return getDayString(dayOfWeek);
    }

    // Return index of the current day (0 for Sunday, 1 for Monday, etc.)
    public static int getCurrentDayIndex() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0, Monday = 1, etc.
    }

    // Return a list of all days of the week
    public static ArrayList<String> getDaysOfWeek() {
        ArrayList<String> days = new ArrayList<>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        return days;
    }

    // Get the day of the week as a string (1 = Sunday, 2 = Monday, etc.)
    private static String getDayString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Unknown";
        }
    }
}
