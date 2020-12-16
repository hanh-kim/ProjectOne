package vn.poly.personalmanagement.methodclass;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import vn.poly.personalmanagement.R;
import vn.poly.personalmanagement.model.Plan;
import vn.poly.personalmanagement.ui.activity.MainActivity;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    final String CHANNEL_ID = "Notification";
    final String CHANNEL_NAME = "Channel Notification";
    final String GROUP_ID = "Group1";
    final String GROUP_NAME = "Group 1";
    NotificationManagerCompat managerCompat;
    Bundle bundle;
    Plan plan;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelGroup channelGroup = new NotificationChannelGroup(GROUP_ID,GROUP_NAME);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is my channel");
            channel.setGroup(GROUP_ID);
            manager.createNotificationChannelGroup(channelGroup);
            manager.createNotificationChannel(channel);
        }

        bundle = intent.getBundleExtra("bundle_key");
        if (bundle != null) {
           plan = (Plan) bundle.getSerializable("plan_key");
        }else return;

        int planId = plan.getId();
        String planName = plan.getPlanName();
        String planDate = plan.getDate();
        String planTime = plan.getTime();
        String planContent = plan.getDescription();
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, planId, intent1, planId);

//        managerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(planName)
                .setColor(Color.BLUE)
                .setContentText("Thời gian vào lúc "+planTime+", ngày "+planDate)
                .setSmallIcon(R.drawable.ic_baseline_notifications)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_baseline_notifications,"Chi tiết",pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText(planDate)
                        .setBigContentTitle(planName)
                        .bigText(planContent)
                );

        manager.notify(planId, builder.build());

    }
}
