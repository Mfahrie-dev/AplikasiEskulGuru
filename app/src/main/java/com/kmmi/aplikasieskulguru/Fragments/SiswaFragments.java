package com.kmmi.aplikasieskulguru.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmmi.aplikasieskulguru.R;

public class SiswaFragments extends Fragment {


    public SiswaFragments() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view    =   inflater.inflate(R.layout.fragment_siswa_fragments, container, false);

        return view;
    }
}