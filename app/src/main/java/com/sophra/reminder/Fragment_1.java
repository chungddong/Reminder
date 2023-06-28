package com.sophra.reminder;

import static com.sophra.reminder.Fragment_2.arraystatic;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_1 extends Fragment {

    Button btn_add_f1;
    Dialog dialog;

    RecyclerView recyclerView;
    F1_RecyclerAdapter adapter;

    ArrayList<String> array = new ArrayList<>();
    private static final String SETTINGS_PLAYER = "quick_noti";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.quick_noti_f1, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        btn_add_f1 = v.findViewById(R.id.btn_add_f1);

        array = getStringArrayPref(getContext(), SETTINGS_PLAYER);

        /*String st = array.get(1);
        String[] ar = st.split("/");
        boolean test = Boolean.parseBoolean(ar[3]);
        Log.v("test_array", "" + test);*/  //이거 지우기

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new F1_RecyclerAdapter();

        Log.v("test_array","" + array); //Arrays.toString(arr)

        /*array.clear();
        setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);*/ //데이터 초기화용 코드

        adapter.notifyDataSetChanged();
        adapter.items.clear();
        for(int i = 0; i < array.size(); i++)
        {
            String str = array.get(i);
            String[] arr = str.split("/");
            adapter.addItem(new Fragment_3.person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),Boolean.parseBoolean(arr[3])));
        }

        /*adapter.addItem(new Fragment_3.person("제목", 12000, System.currentTimeMillis(),true));
        adapter.addItem(new Fragment_3.person("라면", 100000, System.currentTimeMillis(),true));
        adapter.addItem(new Fragment_3.person("우산", 10000, System.currentTimeMillis(),false));
        adapter.addItem(new Fragment_3.person("대충", 50000, System.currentTimeMillis(),false));
        adapter.addItem(new Fragment_3.person("창문", 90000, System.currentTimeMillis(),false));*/

        recyclerView.setAdapter(adapter);

        adapter.setOnItemCLickListener(new F1_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Log.v("time",position + "번 눌림");
                //갖다와서 여기 수정
                boolean isclick = adapter.items.get(position).istrue;
                Log.v("time", "" + isclick);
                long time = System.currentTimeMillis();
                if(isclick == false)  //빠른 알림 시작해야 하는 경우
                {
                    String str = array.get(position);
                    String[] arr = str.split("/");
                    array.remove(position);
                    array.add(position, arr[0] + "/" + arr[1] + "/" + time + "/" + true);
                    setStringArrayPref(getContext(), SETTINGS_PLAYER, array);
                    Calendar cal1 = Calendar.getInstance();
                    //cal1.setTime(date);
                    cal1.setTimeInMillis(Long.parseLong(arr[1]) + time);

                    Log.v("times", "" + cal1.getTimeInMillis());

                    ((MainActivity)MainActivity.mContext).setquickAlarm(cal1, position);
                    adapter.items.clear();
                    //adapter.notifyDataSetChanged();
                    for(int i = 0; i < array.size(); i++)
                    {
                        Log.v("time", "" + array);
                        str = array.get(i);
                        arr = str.split("/");
                        adapter.addItem(new Fragment_3.person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),Boolean.parseBoolean(arr[3])));
                    }
                    adapter.notifyDataSetChanged();
                    isclick = adapter.items.get(position).istrue;
                    Log.v("time", "" + array);

                    int len = array.size();

                    Log.v("times", array + "");

                }
                else if(isclick == true) { //iscliked가 true인 경우 , 빠른 알림을 끄고 초기화하는 경우
                    String str = array.get(position);
                    String[] arr = str.split("/");
                    array.remove(position);
                    array.add(position, arr[0] + "/" + arr[1] + "/" + time + "/" + false);
                    setStringArrayPref(getContext(), SETTINGS_PLAYER, array);
                    adapter.items.clear();
                    for(int i = 0; i < array.size(); i++)
                    {
                        str = array.get(i);
                        arr = str.split("/");
                        adapter.addItem(new Fragment_3.person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),Boolean.parseBoolean(arr[3])));
                    }
                    adapter.notifyDataSetChanged();
                    isclick = adapter.items.get(position).istrue;
                    Log.v("time", "" + isclick);
                    Log.v("time", "" + array);
                    ((MainActivity)MainActivity.mContext).cancelquickAlarm(position);
                }
            }

            @Override
            public void onLongDeleteClick(int position) {
                //Toast.makeText(getContext(), "삭제하시겠습니까" + position, Toast.LENGTH_SHORT).show();
                showDialog(position);
            }
        });

        btn_add_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickDialog();
            }
        });

        return v;
    }

    int min;
    int sec;
    String text;

    public void QuickDialog() {
        dialog = new Dialog(this.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_quick);
        dialog.show();

        // edittext 연결
        EditText et_min_f1 = dialog.findViewById(R.id.et_min_f1);
        EditText et_sec_f1 = dialog.findViewById(R.id.et_sec_f1);
        EditText et_quick_f1 = dialog.findViewById(R.id.et_quick_f1);


        min = 0;
        sec = 0;
        text = "";

        Button btn_cancel = dialog.findViewById(R.id.btn_setdate_cancel); // 다이얼로그에서 취소버튼 누를시
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btn_confirm = dialog.findViewById(R.id.btn_setdate_confirm);   //다이얼로그 확인 버튼 누를시
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_min_f1.length() != 0 || et_sec_f1.length() != 0)
                {
                    if(et_min_f1.length() != 0)
                    {
                        min = Integer.parseInt(String.valueOf(et_min_f1.getText()));
                    }
                    if(et_sec_f1.length() != 0)
                    {
                        sec = Integer.parseInt(String.valueOf(et_sec_f1.getText()));
                    }


                    if(et_quick_f1.length() != 0)  // 빠른 알림 추가 됬을때 - 코드는 아래에 작성
                    {
                        text = String.valueOf(et_quick_f1.getText());
                        Toast.makeText(getContext(), "" + min + "분" + sec + "초" + "," + text , Toast.LENGTH_SHORT).show();

                        long num = 60000;
                        long time = (min * num) + (sec * 1000);
                        //여기에

                        array.add(text + "/" + time + "/" + System.currentTimeMillis() + "/" + false);
                        setStringArrayPref(getContext(), SETTINGS_PLAYER, array);
                        Log.v("test_array", "" + array);

                        //adapter.addItem(new Fragment_3.person("창문", 900000, System.currentTimeMillis()));
                        adapter.items.clear();
                        for(int i = 0; i < array.size(); i++)
                        {
                            Log.v("test_array",""+ "실행함");
                            String str = array.get(i);
                            String[] arr = str.split("/");
                            adapter.addItem(new Fragment_3.person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),Boolean.parseBoolean(arr[3])));
                        }
                        adapter.notifyDataSetChanged();
                        num = 0;
                        time = 0;

                        dialog.dismiss();
                        //Log.v("test_array", "" + et_sec_f1.getText());
                    }
                    else {
                        Toast.makeText(getContext(), "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getContext(), "시간을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showDialog(int position){ //알림 설정 확인 다이얼로그

        dialog = new Dialog(this.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_confirm);
        dialog.show();

        TextView dial_text = dialog.findViewById(R.id.dialog_text);
        TextView title = dialog.findViewById(R.id.dialog_title);


        title.setText("알림 삭제");
        dial_text.setText("선택한 알림이 지워집니다."); //다이얼로그 텍스트 만드는 곳

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

                adapter.items.remove(position);
                array.remove(position);
                setStringArrayPref(getContext(), SETTINGS_PLAYER, array);
                //array = getStringArrayPref(getContext(), SETTINGS_PLAYER_JSON);
                adapter.notifyDataSetChanged();
                adapter.items.clear();
                for(int i = 0; i < array.size(); i++)
                {
                    String str = array.get(i);
                    String[] arr = str.split("/");
                    adapter.addItem(new Fragment_3.person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),Boolean.parseBoolean(arr[3])));
                }

                Toast.makeText(getContext(),"삭제되었습니다!", Toast.LENGTH_SHORT).show();

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
