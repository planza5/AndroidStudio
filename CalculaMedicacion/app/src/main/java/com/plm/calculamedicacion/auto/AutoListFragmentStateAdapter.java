package com.plm.calculamedicacion.auto;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.plm.calculamedicacion.fragments.FragmentCalculations;
import com.plm.calculamedicacion.fragments.FragmentConfig;

public class AutoListFragmentStateAdapter extends FragmentStateAdapter {
    public AutoListFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentCalculations();
            case 1:
                return new FragmentConfig();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
