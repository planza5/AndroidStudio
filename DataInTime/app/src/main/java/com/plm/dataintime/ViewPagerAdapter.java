package com.plm.dataintime;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.plm.dataintime.fragment1.FragmentDataList;
import com.plm.dataintime.fragment2.FragmentDataInTime;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FragmentDataList();
            case 1:
                return new FragmentDataInTime();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
