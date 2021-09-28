package com.kmmi.aplikasieskulguru.Fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kmmi.aplikasieskulguru.Api.EskulApi;
import com.kmmi.aplikasieskulguru.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BuatEskulFragment extends Fragment {

    EditText nama , bayaran, tentang;
    Button tambah, jam;
    Spinner hari;
    TimePickerDialog timePickerDialog ;
    Calendar calendar;
    String var_hari, var_jam;

    public BuatEskulFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view   =   inflater.inflate(R.layout.fragment_buat_eskul, container, false);


        nama    =   view.findViewById(R.id.Edittext_nama);
        bayaran =   view.findViewById(R.id.Edittext_bayaran);
        tentang =  view.findViewById(R.id.Edittext_Tentang);
        tambah  =   view.findViewById(R.id.tambah);
        hari    =   view.findViewById(R.id.spinner_hari);
        jam     =   view.findViewById(R.id.jam);

        hari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    var_hari = adapterView.getItemAtPosition(i).toString();
                }else {
                    var_hari = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                timePickerDialog    =   new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        var_jam  = hour + "." + minute;
                        jam.setText(var_jam);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                timePickerDialog.create();
                timePickerDialog.show();


            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertEskul(view);
            }
        });

        return view;
    }
    private void insertEskul(View view){
        Retrofit retrofit   =   new Retrofit.Builder()
                .baseUrl("https://ublmobilekmmi.web.id/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        EskulApi api    =   retrofit.create(EskulApi.class);
        Call<String> call   =   api.insertEskul(nama.getText().toString(), Integer.parseInt(bayaran.getText().toString()), var_hari, var_jam, tentang.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}