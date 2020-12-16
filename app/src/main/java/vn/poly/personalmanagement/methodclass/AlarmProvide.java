package vn.poly.personalmanagement.methodclass;

import android.app.AlarmManager;
import android.content.Context;

public class AlarmProvide {

    public static synchronized AlarmManager getAlarmManager(Context context){
        AlarmManager alarmManager = null;
        if (alarmManager == null){
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }
}
