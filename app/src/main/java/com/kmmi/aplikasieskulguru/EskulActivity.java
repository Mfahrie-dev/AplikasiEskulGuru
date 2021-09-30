package com.kmmi.aplikasieskulguru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kmmi.aplikasieskulguru.Api.EskulApi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EskulActivity extends AppCompatActivity {

    EditText nama , bayaran, tentang;
    Button tambah, jam;
    Spinner hari;
    TimePickerDialog timePickerDialog ;
    Calendar calendar;
    String var_hari, var_jam,uri_foto, tanggal;
    Uri path;
    ImageView foto_eskul;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eskul);

        nama    =   findViewById(R.id.Edittext_nama);
        bayaran =   findViewById(R.id.Edittext_bayaran);
        tentang =   findViewById(R.id.Edittext_Tentang);
        tambah  =   findViewById(R.id.tambah);
        hari    =   findViewById(R.id.spinner_hari);
        jam     =   findViewById(R.id.jam);
        foto_eskul= findViewById(R.id.foto);
        storage  = FirebaseStorage.getInstance();

        foto_eskul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });


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
                timePickerDialog    =   new TimePickerDialog(EskulActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        var_jam  = hour + "." + minute;
                        jam.setText(var_jam);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.create();
                timePickerDialog.show();


            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFoto();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 100:
                    try {
                        path = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), path);
                        foto_eskul.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }

    private void insertEskul(){
        Retrofit retrofit   =   new Retrofit.Builder()
                                        .baseUrl(getString(R.string.base_url))
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .build();
        EskulApi api    =   retrofit.create(EskulApi.class);
        Call<String> call   =   api.insertEskul(nama.getText().toString(), Integer.parseInt(bayaran.getText().toString()), var_hari, var_jam, tentang.getText().toString(), uri_foto);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void uploadFoto(){
        progressDialog = new ProgressDialog(EskulActivity.this);
        progressDialog.setTitle("Membuat Eskul");
        progressDialog.setMessage("Mengupload Foto.....");
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();
        calendar    =   Calendar.getInstance();
        tanggal     =   new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(calendar.getTime());
        reference   =   storage.getReference("fotoEskul/" + tanggal  );
        UploadTask task = reference.putFile(path);

        Task<Uri> upload = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    uri_foto = task.getResult().toString();
                    insertEskul();
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}