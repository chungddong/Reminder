package com.sophra.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AlarmRecevier extends BroadcastReceiver {

    //노티 알림을 위한 리시버임

    public static String title = "";
    public static String content = "";
    public static int id;

    ArrayList<String> alarm_arr = new ArrayList<>();

    public AlarmRecevier(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    ArrayList<String> array = new ArrayList<>();
    private static final String SETTINGS_PLAYER_JSON = "settings_item";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        array = getStringArrayPref(context, SETTINGS_PLAYER_JSON);

        ArrayList<String> alarm_arr = new ArrayList<>();

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);


        long time = System.currentTimeMillis();

        for(int i = 0; i < array.size(); i++)
        {
            String str = array.get(i);
            String[] arr = str.split("/");
            alarm_arr.add(arr[1]);
        }

        int num = 0;

        long times = System.currentTimeMillis();

        long tes = System.currentTimeMillis() - Long.parseLong(alarm_arr.get(2));

        for(int j = 0; j < alarm_arr.size(); j++)
        {
            long test = System.currentTimeMillis() - Long.parseLong(alarm_arr.get(j));
            if(test < 0)
            {
                test *= -1;
            }
            alarm_arr.set(j,String.valueOf(test));
        }

        long min = Long.parseLong(alarm_arr.get(0));

        for(int j = 0; j < alarm_arr.size(); j++)
        {
            long test = Long.parseLong(alarm_arr.get(j));
            if(test < min)
            {
                min = test;
                num = j;
            }
        }


        //Log.v("times", alarm_arr + "" + ", min : " + min + ", num : " + num + "시간 " + System.currentTimeMillis());

        String str = array.get(num);
        String[] arr = str.split("/");

        Log.v("times", str + "");

        content = arr[0];

        // 이제 됐으니 오류 없도록 더 깔끔하게 고치기

        //알림창 제목
        builder.setContentTitle(title);
        builder.setContentText(content);
        //알림창 아이콘
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(num,notification);

    }

    private ArrayList getStringArrayPref(Context context, String key) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList urls = new ArrayList();

        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);

                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}
