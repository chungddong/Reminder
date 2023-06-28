package com.sophra.reminder;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerAdpater extends RecyclerView.Adapter<RecyclerAdpater.ViewHolder>{

    // 레이아웃 f_3에 알림 리스트를 위한 리사이클러뷰 어댑터임

    ArrayList<Fragment_3.person> items = new ArrayList<Fragment_3.person>();

    private Handler mHandler = new Handler();
    public CountDownTimer timer;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
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

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            tvPersonName = itemView.findViewById(R.id.tvPersonName);
            tvPersonPhoneNumber = itemView.findViewById(R.id.tvPersonPhoneNumber);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            progressBar = itemView.findViewById(R.id.item_progress);


            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        public void setItem(Fragment_3.person item) {
            tvPersonName.setText(item.getName());
            tvPersonName.setTextColor(Color.parseColor("#B3D2FF"));
            String string = String.valueOf((int) item.getExpirationTime());
            tvPersonPhoneNumber.setText(string);
            per = item;
            time_percent = per.expirationTime - item.getCreate_time();
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = per.expirationTime - currentTime;  //남은시간
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int day = (int) (timeDiff / (1000 * 60 * 60 * 24));
                //몇년 남았는지도 계산되게 넣어야됨 - 업데이트

                int month = day / 31;
                if(day >= 31)
                {
                    day -= month * 31;
                }
                if(month == 0)
                {
                    if(day == 0)
                    {
                        if(hours == 0)
                        {
                            if(minutes == 0)
                            {
                                tvPersonPhoneNumber.setText(seconds + " 초");
                            }
                            else {tvPersonPhoneNumber.setText(minutes + " 분 " + seconds + " 초");}
                        }
                        else {tvPersonPhoneNumber.setText(hours + " 시간 " + minutes + " 분 " + seconds + " 초");}
                    }
                    else {tvPersonPhoneNumber.setText(day + " 일 " + hours + " 시간 " + minutes + " 분 " + seconds + " 초");}
                }
                else {tvPersonPhoneNumber.setText(month + " 달 " + day + " 일 " + hours + " 시간 " + minutes + " 분 " + seconds + " 초");}
                //tvPersonPhoneNumber.setText(day + " 일 " + hours + " 시간 " + minutes + " 분 " + seconds + " 초");
                int percent = (int) ((double) (timeDiff / 1000) / (double) (time_percent / 1000) * 10000);
                progressBar.setProgress(percent);
                //Log.v("test","" + percent); // 로그 확인용
            } else {
                tvPersonName.setTextColor(Color.parseColor("#3E3E3E"));
                tvPersonPhoneNumber.setText("종료됨"); //타이머 작동이 끝났을 때
                progressBar.setProgress(0);
            }
        }

    }


    // 뷰홀더가 새로 만들어질 때 호출된다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 파라미터로 전달되는 뷰그룹 객체는 각 아이템을 위한 뷰그룹 객체이므로
        // XML 레이아웃을 인플레이션하여 이 뷰그룹 객체에 전달한다.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);



        return new ViewHolder(itemView, mlistener);
    }

    // 뷰홀더가 재사용될 때 호출된다. 이 메서드는 재활용할 수 있는 뷰홀더 객체를 파라미터로 전달한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 뷰 객체는 기존 것을 그대로 사용하고 데이터만 바꿔준다.
        Fragment_3.person item = items.get(position);
        holder.setItem(item);
        //Log.v("test","" + items.get(2).getExpirationTime()); // 로그 확인용

        int time_percent = (int) (items.get(position).expirationTime - items.get(position).create_time);
        long timeDiff =  (items.get(position).expirationTime - System.currentTimeMillis());
        int percent = (int) ((double) (timeDiff / 1000) / (double) (time_percent / 1000) * 10000);


        Log.v("testing","" + timeDiff); // 로그 확인용



        holder.progressBar.setProgress(percent);

        // 이 위로 주석처리한거 시간에 따른 퍼센트 안 맞는거 해결하려고 놔둔거 - 이따가 와서 해결할것



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


}
