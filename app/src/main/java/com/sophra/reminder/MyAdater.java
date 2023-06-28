package com.sophra.reminder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdater extends FragmentStateAdapter {

    //프래그먼트 이동을 위한 어댑터임

    public int count;

    public MyAdater(FragmentActivity fa, int page) {
        super(fa);
        count = page;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new Fragment_1();
        else if(index==1) return new Fragment_2();
        else if(index==2) return new Fragment_3();
        else return null;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % count; }
}
