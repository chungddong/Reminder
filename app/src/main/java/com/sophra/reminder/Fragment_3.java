package com.sophra.reminder;

import static com.sophra.reminder.Fragment_2.arraystatic;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_3 extends Fragment {


    public static Context context_f3;

    RecyclerView recyclerView;
    RecyclerAdpater adapter;

    Dialog dialog;

    ArrayList<String> array = new ArrayList<>();
    private static final String SETTINGS_PLAYER_JSON = "settings_item";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.noti_list_f3, container, false);

        context_f3 = this.getContext();

        recyclerView = v.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdpater();

        //setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);

        array = getStringArrayPref(getContext(), SETTINGS_PLAYER_JSON);
        arraystatic = array;

        adapter.notifyDataSetChanged();
        adapter.items.clear();
        for(int i = 0; i < array.size(); i++)
        {
            String str = array.get(i);
            String[] arr = str.split("/");
            adapter.addItem(new person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),false));
        }

        //adapter.addItem(new person("대충 제목", System.currentTimeMillis() + 120000, System.currentTimeMillis()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemCLickListener(new RecyclerAdpater.OnItemClickListener() {  //삭제하는 버튼 눌렸을때

            @Override
            public void onDeleteClick(int position) {
                showDialog(position);
            }
        });


        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        //Log.v("test_array", "새로고침");
        Log.v("test_array","" + array  + "새로고침");
        //Log.v("test_array", ""+arraystatic);
        reload();
    }

    public void reload() {
        //array = getStringArrayPref(getContext(), SETTINGS_PLAYER_JSON);
        array = arraystatic;
        //Log.v("test_array", "실행");
        adapter.items.clear();
        for(int i = 0; i < array.size(); i++)
        {
            //Log.v("test_array",""+ "실행함");
            //Toast.makeText(getContext(), "시간다됨", Toast.LENGTH_SHORT).show();
            String str = array.get(i);
            String[] arr = str.split("/");
            adapter.addItem(new person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),false));
        }
        if(array.size() == 1)
        {
            adapter.notifyDataSetChanged();
        }
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

    static class person {
        String name;
        long expirationTime;
        long create_time;
        boolean istrue;

        public person(String name, long expirationTime, long create_time, boolean istrue) {
            this.name = name;
            this.expirationTime = expirationTime;
            this.create_time = create_time;
            this.istrue = istrue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
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
                setStringArrayPref(getContext(), SETTINGS_PLAYER_JSON, array);
                //array = getStringArrayPref(getContext(), SETTINGS_PLAYER_JSON);
                adapter.notifyDataSetChanged();
                adapter.items.clear();
                Log.v("test_array","" + array);
                for(int i = 0; i < array.size(); i++)
                {
                    String str = array.get(i);
                    String[] arr = str.split("/");
                    adapter.addItem(new person(arr[0],Long.parseLong(arr[1]),Long.parseLong(arr[2]),false));
                }

                Toast.makeText(getContext(),"삭제되었습니다!", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                arraystatic = array;

            }
        });
    }

}


