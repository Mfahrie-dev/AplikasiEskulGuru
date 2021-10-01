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
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kmmi.aplikasieskulguru.Api.AdminApi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdminActivity extends AppCompatActivity {

    private EditText level, nama, username, password;
    private CircleImageView fotoAdmin;
    private Uri path;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference reference;
    Calendar calendar;
    private String tanggal, uri_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        level           =   findViewById(R.id.Edittext_level);
        nama            =   findViewById(R.id.Edittext_nama);
        username        =   findViewById(R.id.Edittext_username);
        password        =   findViewById(R.id.Edittext_password);
        fotoAdmin       =   findViewById(R.id.circleImageView_fotoadmin);
        storage         =   FirebaseStorage.getInstance();

        fotoAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
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
                        fotoAdmin.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }

    public void buatAdmin(View view) {
        uploadFoto();
    }
    private void uploadFoto(){
        progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setTitle("Membuat Admin");
        progressDialog.setMessage("Mengupload Foto.....");
        progressDialog.setCancelable(false);
        progressDialog.create();
        progressDialog.show();
        calendar    =   Calendar.getInstance();
        tanggal     =   new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(calendar.getTime());
        reference   =   storage.getReference("fotoAdmin/" + tanggal  );
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
                    insertAdmin();
                    if (progressDialog.isShowing()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 1500);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void insertAdmin(){
        Retrofit retrofit   =   new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        AdminApi    api = retrofit.create(AdminApi.class);
        Call<String> call   =   api.insertAdmin(username.getText().toString(), password.getText().toString(), nama.getText().toString(), uri_foto, Integer.parseInt(level.getText().toString()));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}