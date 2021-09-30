package com.kmmi.aplikasieskulguru;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kmmi.aplikasieskulguru.Api.EskulApi;
import com.kmmi.aplikasieskulguru.Api.PengumumanApi;
import com.kmmi.aplikasieskulguru.Model.EskulModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PengumumanActivity extends AppCompatActivity {

    EditText judul, isi;
    Spinner eskul;
    ImageView fotoPengumuman;
    List<EskulModel> listEskul;
    String ideskul, tanggal, uri_foto;
    Uri path;
    String array[];
    Button buat;
    FirebaseStorage storage;
    StorageReference reference;
    ProgressDialog progressDialog;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);


        judul           =   findViewById(R.id.Edittext_judul);
        isi             =   findViewById(R.id.Edittext_isi);
        eskul           =   findViewById(R.id.spinner_eskul);
        fotoPengumuman  =   findViewById(R.id.foto);
        buat            =   findViewById(R.id.buat);
        storage         =   FirebaseStorage.getInstance();
        getDataEskul();

        eskul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    ideskul  =   adapterView.getItemAtPosition(i).toString();
                    array    =   ideskul.split("--");
                    Toast.makeText(getApplicationContext(), array[0], Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fotoPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        buat.setOnClickListener(new View.OnClickListener() {
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
                        fotoPengumuman.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }
    private void getDataEskul(){
        Retrofit retrofit   =   new Retrofit.Builder()
                                        .baseUrl(getString(R.string.base_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
        EskulApi api        =   retrofit.create(EskulApi.class);
        Call<List<EskulModel>> call = api.getdataEskul();
        listEskul   =   new ArrayList<>();
        call.enqueue(new Callback<List<EskulModel>>() {
            @Override
            public void onResponse(Call<List<EskulModel>> call, Response<List<EskulModel>> response) {
                if (response.isSuccessful()){
                    listEskul   =   response.body();
                    ArrayList<String> list = new ArrayList<>();
                    list.add("Tujuan Eskul");
                    for (int i=0; i< listEskul.size(); i++){
                        list.add(listEskul.get(i).getId_eskul() + "--" + listEskul.get(i).getNama_eskul());
                    }
                    buatSpinnerEskul(list);

                }else {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EskulModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void buatSpinnerEskul(ArrayList<String> list){
        eskul.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list));
        ((ArrayAdapter)eskul.getAdapter()).notifyDataSetChanged();
    }

    private void insertPengumuman(){
        Retrofit retrofit   =   new Retrofit.Builder()
                                        .baseUrl(getString(R.string.base_url))
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .build();
        PengumumanApi api        =       retrofit.create(PengumumanApi.class);
        Call<String> call        =       api.insertPengumuman(judul.getText().toString(), isi.getText().toString(), "yes", uri_foto, Integer.parseInt(array[0]));

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
        progressDialog = new ProgressDialog(PengumumanActivity.this);
        progressDialog.setTitle("Membuat Pengumuman");
        progressDialog.setMessage("Mengupload Foto.....");
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();
        calendar    =   Calendar.getInstance();
        tanggal     =   new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(calendar.getTime());
        reference   =   storage.getReference("fotoPengumuman/" + tanggal  );
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
                    insertPengumuman();
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}