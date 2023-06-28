package com.sophra.reminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class F1_RecyclerAdapter extends RecyclerView.Adapter<F1_RecyclerAdapter.ViewHolder> {

    ArrayList<Fragment_3.person> items = new ArrayList<Fragment_3.person>();

    private Handler mHandler = new Handler();
    public CountDownTimer timer;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onLongDeleteClick(int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPersonName;
        TextView tvPersonPhoneNumber;
        Button btn_delete;
        Fragment_3.person per;
        ProgressBar progressBar;

        long time_percent;
        boolean isturned;
        boolean isclicked;

        long expiration;

        ArrayList<String> array = new ArrayList<>();
        private static final String SETTINGS_PLAYER = "quick_noti";

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            tvPersonName = itemView.findViewById(R.id.tvPersonName);
            tvPersonPhoneNumber = itemView.findViewById(R.id.tvPersonPhoneNumber);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            progressBar = itemView.findViewById(R.id.item_progress);

            //isturned = false;
            isclicked = false;
            //isturned = per.istrue;

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.v("time", "누름" + listener);
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            btn_delete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongDeleteClick(getAdapterPosition());
                    return false;
                }
            });


        }

        public void setItem(Fragment_3.person item) {
            tvPersonName.setText(item.getName());
            String string = String.valueOf((int) item.getExpirationTime());
            tvPersonPhoneNumber.setText(string);
            per = item;
            expiration = per.expirationTime;
            time_percent = expiration;
            isturned = per.istrue;

            //Log.v("time", "" + isturned);

            if(isturned != true)
            {
                progressBar.setProgress(10000);

                int seconds = (int) (expiration / 1000) % 60;
                int minutes = (int) ((expiration / (1000 * 60)) % 60);
                tvPersonPhoneNumber.setText(minutes + " 분 " + seconds + " 초");
            }

            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {

            if(isturned == true)
            {
                //Log.v("time", "켜짐");
                long timeDiff = (expiration + per.create_time)- currentTime;  //남은시간
                if (timeDiff > 0) {
                    int seconds = (int) (timeDiff / 1000) % 60;
                    int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                    int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                    tvPersonPhoneNumber.setText("");
                    if(hours != 0)
                    {
                        tvPersonPhoneNumber.setText(hours + " 시간 ");
                    }
                    //tvPersonPhoneNumber.setText(tvPersonPhoneNumber.getText() + "fdsf");
                    tvPersonPhoneNumber.setText((String)tvPersonPhoneNumber.getText() + minutes + " 분 " + seconds + " 초");
                    int percent = (int) ((double) (timeDiff / 1000) / (double) (time_percent / 1000) * 10000);
                    progressBar.setProgress(percent);
                    //Log.v("test","" + percent); // 로그 확인용
                } else {
                    tvPersonPhoneNumber.setText("종료됨"); //타이머 작동이 끝났을 때
                    progressBar.setProgress(0);
                }
            }
            else {
                //Log.v("time", "꺼짐");
            }

        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 파라미터로 전달되는 뷰그룹 객체는 각 아이템을 위한 뷰그룹 객체이므로
        // XML 레이아웃을 인플레이션하여 이 뷰그룹 객체에 전달한다.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.grid_item_f1, parent, false);

        return new ViewHolder(itemView, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 뷰 객체는 기존 것을 그대로 사용하고 데이터만 바꿔준다.
        Fragment_3.person item = items.get(position);
        holder.setItem(item);

        if(items.get(position).istrue == true)
        {
            //Log.v("time", "istrue 켜짐");
            int time_percent = (int) (holder.expiration);
            long timeDiff =  (holder.expiration + items.get(position).create_time) - System.currentTimeMillis();
            int percent = (int) ((double) (timeDiff / 1000) / (double) (time_percent / 1000) * 10000);

            //Log.v("test_array","" + items.get(position).name + items.get(position).expirationTime); // 로그 확인용
            //Log.v("test_array","" + percent);

            holder.progressBar.setProgress(percent);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                Runnable updateRemainingTimeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        synchronized (mHandler) {
                            long currentTime = System.currentTimeMillis();
                            holder.updateTimeRemaining(currentTime);
                        }
                    }
                };

                @Override
                public void run() {
                    mHandler.post(updateRemainingTimeRunnable);
                }
            }, 100, 100);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Fragment_3.person item) {
        items.add(item);
    }

    public void setItems(ArrayList<Fragment_3.person> items) {
        this.items = items;
    }

    public Fragment_3.person getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Fragment_3.person item) {
        items.set(position, item);
    }

    private static void setStringArrayPref(Context context, String key, ArrayList<String> values) {

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

    private static ArrayList getStringArrayPref(Context context, String key) {

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
