package vn.poly.personalmanagement.methodclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeFormat {

    public static String parseDate(Calendar calendar) {
        String date = "";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date += format.format(calendar.getTime());
        return date;
    }

    public static String parseTime(Calendar calendar) {
        String time = "";
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("HH:mm");
        time += format.format(calendar.getTime());
        return time;
    }

    public static Calendar parseDateToCalendar(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] arrDate = date.split("/");
        int day = Integer.parseInt(arrDate[0]);
        int month = Integer.parseInt(arrDate[1])-1;
        int year = Integer.parseInt(arrDate[2]);
        //   calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    public static Calendar parseTimeToCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        String[] arrTime = time.split(":");
        int hour = Integer.parseInt(arrTime[0]);
        int minute = Integer.parseInt(arrTime[1]);
        //  calendar.set(0, 0, 0, hour, minute, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static Calendar parseCalendar(String time, String date) {
        Calendar calendar = Calendar.getInstance();
        String[] arrTime = time.split(":");
        String[] arrDate = date.split("/");
        int hour = Integer.parseInt(arrTime[0]);
        int minute = Integer.parseInt(arrTime[1]);
        int day = Integer.parseInt(arrDate[0]);
        int month = Integer.parseInt(arrDate[1])-1;
        int year = Integer.parseInt(arrDate[2]);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

}
