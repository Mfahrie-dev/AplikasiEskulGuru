package com.kmmi.aplikasieskulguru.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kmmi.aplikasieskulguru.Fragments.BuatEskulFragment;
import com.kmmi.aplikasieskulguru.Fragments.PengumunanFragment;

public class PagetAdapter extends FragmentStatePagerAdapter {

    Context context;
    int itemCount;

    public PagetAdapter(@NonNull FragmentManager fm, Context context, int itemCount) {
        super(fm);
        this.context = context;
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            return  new BuatEskulFragment();
        }
        if (position == 1){
            return  new PengumunanFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0){
            return "Buat Eskul";
        }
        if (position == 1){
            return "Pengumuman";
        }

        return null;
    }
}
