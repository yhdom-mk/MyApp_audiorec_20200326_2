package com.example.myapp_audiorec_20200326_2;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public String getTimeAgo(long duration) {
        Date now = new Date();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - duration);
        long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - duration);
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - duration);

        if (seconds < 60) {
            return "JUST NOW !";
        } else if (minutes == 1) {
            return "A MINUTE ago";
        } else if (minutes > 1 && minutes < 60) {
            return minutes + " MINUTES ago";
        } else if (hours == 1) {
            return "1 HOUR ago";
        } else if (hours > 1 && hours < 24) {
            return hours + " HOURS ago";
        } else if (days == 1) {
            return "1 DAY ago";
        } else {
            return days + " DAYS ago";
        }
    }
}
