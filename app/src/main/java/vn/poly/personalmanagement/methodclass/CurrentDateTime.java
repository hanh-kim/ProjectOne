package vn.poly.personalmanagement.methodclass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentDateTime {
    public static String getCurrentDate() {
        /* format date: 01/01/2020 */
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd/MM/yyyy");
        calendar.set(year, month, day);
        String date = simpleDateFormat.format( calendar.getTime());

        /* format date: Jan 01,2020 */
//        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
//        date += simpleDateFormat.format( calendar.getTime());
        return date;
    }
    public static String getCurrentTime() {
        String time="";
        /* format hour:  00:00:00 */
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
        time += simpleDateFormat.format( calendar.getTime());// có định dang: hh:mm:ss
        return time;


//        String time="";
//        Calendar calendar = Calendar.getInstance();
//        int hour= calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//        simpleDateFormat.applyPattern("HH:mm");
//        calendar.set(hour,minute);
//        time += simpleDateFormat.format( calendar.getTime());
//        return time;

    }
}
