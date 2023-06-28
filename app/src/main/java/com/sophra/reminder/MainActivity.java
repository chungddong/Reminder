package com.sophra.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private int page = 3;
    private CircleIndicator3 indicator;

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    public static Context mContext;

    AdRequest request;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        request = new AdRequest.Builder().build();

        //뷰페이저
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new MyAdater(this, page); //어댑터
        viewPager.setAdapter(pagerAdapter);
        //인디케이터 설정
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.createIndicators(page,0);
        //뷰페이저 설정
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager.setCurrentItem(4,false);
        viewPager.setOffscreenPageLimit(3);
        indicator.animatePageSelected(1);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffsetPixels == 0) {
                    viewPager.setCurrentItem(position);
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position%page);
            }
        });

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();

        Log.v("HelloAlarmActivity", mCalender.getTime().toString());


        //setAlarm();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ads);


        TextView quit = dialog.findViewById(R.id.btn_ads_finish);
        TextView cancel = dialog.findViewById(R.id.btn_ads_cancel);

        AdView adView = dialog.findViewById(R.id.dialog_ads_ad);
        //AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //adview = find

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if(!rect.contains(x,y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(),0);
                }
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    int code = 0;

    public void setAlarm(Calendar calendar) {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmRecevier.class);
        receiverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, code, receiverIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        //alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
        alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);


        code += 1;

        Toast.makeText(getApplicationContext(),"추가하였습니다!" + code, Toast.LENGTH_SHORT).show();
    }

    public void setquickAlarm(Calendar calend, int id){
        Intent receiverIntent = new Intent(MainActivity.this, quickAlarmRecevier.class);
        receiverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, receiverIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        //Log.v("times", "메인에서 시간 : " + calend.getTimeInMillis());
        alarmManager.setExact(AlarmManager.RTC, calend.getTimeInMillis(),pendingIntent);

        //Toast.makeText(getApplicationContext(),"추가하였습니다!" + code, Toast.LENGTH_SHORT).show();
    }

    public void cancelquickAlarm(int id){
        Intent receiverIntent = new Intent(MainActivity.this, quickAlarmRecevier.class);
        receiverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, receiverIntent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();

        Log.v("times", "알림 취소됨 번호 : " + id);
    }

    @Override
    public void onBackPressed() {


        dialog.show();


    }
}