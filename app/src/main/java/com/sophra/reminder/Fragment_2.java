package com.sophra.reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Fragment_2 extends Fragment {

    RecyclerAdpater adpater;

    EditText et_day_f2;
    EditText et_hour_f2;
    EditText et_minutes_f2;
    EditText et_second_f2;

    EditText et_memo_f2;

    Button btn_add_f2;
    Button btn_setdate_f2;

    private int day = 0;
    private int hour = 0;
    private int min = 0;
    private int sec = 0;

    Dialog dialog;
    String memo;

    ArrayList<String> array = new ArrayList<>();
    static ArrayList<String> arraystatic = new ArrayList<>();
    private static final String SETTINGS_PLAYER_JSON = "settings_item";
    private static final String ALARMS = "alarm_arr";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.noti_main_f2, container, false);*/

        //return rootView;
        View v = inflater.inflate(R.layout.noti_main_f2, container, false);

        et_day_f2 = v.findViewById(R.id.et_day_f2);
        et_hour_f2 = v.findViewById(R.id.et_hour_f2);
        et_minutes_f2 = v.findViewById(R.id.et_minutes_f2);
        et_second_f2 = v.findViewById(R.id.et_second_f2);
        et_memo_f2 = v.findViewById(R.id.et_memo_f2);

        array = getStringArrayPref(this.getContext(), SETTINGS_PLAYER_JSON);
        arraystatic = array;
        //Log.v("test_array","" + array);

        et_day_f2.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                et_day_f2.setTextColor(Color.parseColor("#3F86EB"));
                if(et_day_f2.length() == 0)
                {
                    et_day_f2.setText("00");
                    et_day_f2.setTextColor(Color.parseColor("#000000"));
                }
                if(Integer.parseInt(et_day_f2.getText().toString()) >= 32)
                {
                    et_day_f2.setText("31");
                    et_day_f2.setSelection(et_day_f2.length());
                }
                day = Integer.parseInt(et_day_f2.getText().toString());
            }
        });

        et_hour_f2.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                et_hour_f2.setTextColor(Color.parseColor("#3F86EB"));
                if(et_hour_f2.length() == 0)
                {
                    et_hour_f2.setText("00");
                    et_hour_f2.setTextColor(Color.parseColor("#000000"));
                }
                if(Integer.parseInt(et_hour_f2.getText().toString()) >= 24)
                {
                    et_hour_f2.setText("23");
                    et_hour_f2.setSelection(et_hour_f2.length());
                }
                hour = Integer.parseInt(et_hour_f2.getText().toString());
            }
        });

        et_minutes_f2.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                et_minutes_f2.setTextColor(Color.parseColor("#3F86EB"));
                if(et_minutes_f2.length() == 0)
                {
                    et_minutes_f2.setText("00");
                    et_minutes_f2.setTextColor(Color.parseColor("#000000"));
                }
                if(Integer.parseInt(et_minutes_f2.getText().toString()) >= 60)
                {
                    et_minutes_f2.setText("59");
                    et_minutes_f2.setSelection(et_minutes_f2.length());
                }
                min = Integer.parseInt(et_minutes_f2.getText().toString());
            }
        });

        et_second_f2.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                et_second_f2.setTextColor(Color.parseColor("#3F86EB"));
                if(et_second_f2.length() == 0)
                {
                    et_second_f2.setText("00");
                    et_second_f2.setTextColor(Color.parseColor("#000000"));
                }
                if(Integer.parseInt(et_second_f2.getText().toString()) >= 60)
                {
                    et_second_f2.setText("59");
                    et_second_f2.setSelection(et_second_f2.length());
                }
                sec = Integer.parseInt(et_second_f2.getText().toString());
            }
        });

        et_memo_f2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                memo = et_memo_f2.getText().toString();
            }
        });



        btn_add_f2 = v.findViewById(R.id.btn_add_f2);
        btn_add_f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"알림추가 버튼 누름", Toast.LENGTH_SHORT).show();
                if(day !=0 || hour !=0 || min !=0 || sec !=0)
                {
                    long num = 60000;
                    long time = (day * 24 * 60 * num) + (hour * 60 * num) + (min * num) + (sec * 1000);
                    showDialog(time);
                }
                else
                {
                    Toast.makeText(getContext(),"시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_setdate_f2 = v.findViewById(R.id.btn_setdate_f2);
        btn_setdate_f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateDialog();
            }
        });

        //여기다가




        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        array = arraystatic;
        setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);
    }

    public void showDialog(long time){ //알림 설정 확인 다이얼로그

        dialog = new Dialog(this.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_confirm);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.dialog_text);
        //Toast.makeText(getContext(),"" + textView.getText(), Toast.LENGTH_SHORT).show();
        //textView.setText("1일 12시간 2분 30초 뒤에 '라면 다 됨' 알림을 설정합니다");
        String string = "";

        if(day > 0)
        {
            string += day + "일";
        }
        if(hour > 0)
        {
            string += hour + "시간";
        }
        if(min > 0)
        {
            string += min + "분";
        }
        if(sec > 0)
        {
            string += sec + "초";
        }
        if(memo == null)
        {
            memo = "내용 없음";
        }

        textView.setText(string + " " + "뒤에 " + "'" + memo + "'" + " 알림을 설정합니다."); //다이얼로그 텍스트 만드는 곳

        Button btn_cancel = dialog.findViewById(R.id.btn_add_cancel); // 다이얼로그에서 취소버튼 누를시
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btn_confirm = dialog.findViewById(R.id.btn_add_confirm);   //알림 추가 확인 다이얼로그 확인 버튼 누를시 -> 데이터 리스트뷰로 보내기
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int len = array.size();

                AlarmRecevier.id = len;

                Date date = new Date(System.currentTimeMillis());
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                cal1.add(Calendar.DATE,day);
                cal1.add(Calendar.HOUR_OF_DAY, hour);
                cal1.add(Calendar.MINUTE, min);
                cal1.add(Calendar.SECOND, sec);

                AlarmRecevier.title = memo;

                long expirationTime = System.currentTimeMillis() + time;
                array.add(memo + "/" + expirationTime + "/" + System.currentTimeMillis() + "/" + false);
                //setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);

                //array = getStringArrayPref(getContext(), SETTINGS_PLAYER_JSON);
                setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);
                Log.v("times", "" + array);
                Log.v("times", "" + cal1.getTimeInMillis());

                ((MainActivity)MainActivity.mContext).setAlarm(cal1);

                Toast.makeText(getContext(),"" + date, Toast.LENGTH_SHORT).show();

                Toast.makeText(getContext(),"추가하였습니다!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    public void setDateDialog() {
        dialog = new Dialog(this.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_setting_date);
        dialog.show();

        Date date = new Date();
        final int[] years = {date.getYear()};
        years[0] += 1900; //Date는 1900 이후부터 카운트 되므로 더해줘야함
        final int[] months = {date.getMonth()};
        months[0] += 1; // 1월이 0부터 시작하므로 1 더해야 함
        final int[] day = {date.getDate()};
        int hour = date.getHours();
        final int[] min = {date.getMinutes()};

        Button btn_set = dialog.findViewById(R.id.btn_set);
        Button btn_set_time = dialog.findViewById(R.id.btn_set_time);

        btn_set.setText(years[0] + "년 " + months[0] + "월 " + day[0] + "일 ");

        final int[] finalHour = {hour};

        if(hour >= 12)
        {
            hour -= 12;
            btn_set_time.setText("오후 " + hour + "시 " + min[0] + "분");
        }
        else {
            btn_set_time.setText("오전 " +hour + "시 " + min[0] + "분");
        }


        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;  //1월이 0부터 시작하므로 1 더해줘야 맞음
                btn_set.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth +"일 ");
                years[0] = year;
                months[0] = monthOfYear;
                day[0] = dayOfMonth;
            }
        };

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datepicker = new DatePickerDialog(getContext(), listener, years[0], months[0] - 1, day[0]);
                datepicker.show();

            }
        });

        btn_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if(i >= 12)
                        {
                            finalHour[0] = i;
                            i -= 12;
                            btn_set_time.setText("오후 " + i + "시 " + i1 + "분");
                            min[0] = i1;
                        }
                        else {
                            btn_set_time.setText("오전 " + i + "시 " + i1 + "분");
                            finalHour[0] = i;
                            min[0] = i1;
                        }
                    }
                }, finalHour[0], min[0],false);
                timePickerDialog.show();
            }
        });

        Button btn_cancel = dialog.findViewById(R.id.btn_setdate_cancel); // 다이얼로그에서 취소버튼 누를시
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btn_confirm = dialog.findViewById(R.id.btn_setdate_confirm);   //알림 추가 확인 다이얼로그 확인 버튼 누를시 -> 데이터 리스트뷰로 보내기
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"설정함!", Toast.LENGTH_SHORT).show();
                years[0] -= 1900;
                months[0] -= 1;
                date.setYear(years[0]);
                date.setMonth(months[0]);
                date.setDate(day[0]);
                date.setHours(finalHour[0]);
                date.setMinutes(min[0]);
                String dates = years[0] + "년" + months[0] + "월" + day[0] + "일" + finalHour[0] + "시" + min[0] + "분";

                if(memo == null)
                {
                    memo = "내용 없음";
                }

                long expirationTime = date.getTime();

                int len = array.size();

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                /*cal1.add(Calendar.DATE,day[0]);
                cal1.add(Calendar.HOUR_OF_DAY, finalHour[0]);
                cal1.add(Calendar.MINUTE, min[0]);*/

                AlarmRecevier.title = memo;

                ((MainActivity)MainActivity.mContext).setAlarm(cal1);

                array.add(memo + "/" + expirationTime + "/" + System.currentTimeMillis() + "/" + false);

                setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);

                //((MainActivity)MainActivity.mContext).setAlarm(cal1);

                Toast.makeText(getContext(),"추가하였습니다!", Toast.LENGTH_SHORT).show();

                //Log.v("time", "" + expirationTime  + " " + "현재시간 : " + System.currentTimeMillis());
                dialog.dismiss();
            }
        });
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();

        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }

        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }

        editor.apply();
        //editor.clear();
        //editor.commit();
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
