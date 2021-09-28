package com.kmmi.aplikasieskulguru.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kmmi.aplikasieskulguru.R;

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

public class PengumunanFragment extends Fragment {

    EditText judul, isi;
    Spinner status,eskul;
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

    public PengumunanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view   =   inflater.inflate(R.layout.fragment_pengumunan, container, false);

        judul           =   view.findViewById(R.id.Edittext_judul);
        isi             =   view.findViewById(R.id.Edittext_isi);
        status          =   view.findViewById(R.id.spinner_status);
        eskul           =   view.findViewById(R.id.spinner_eskul);
        fotoPengumuman  =   view.findViewById(R.id.foto);
        buat            =   view.findViewById(R.id.buat);
        storage         =   FirebaseStorage.getInstance();
        getDataEskul();

        eskul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0){
                    ideskul  =   adapterView.getItemAtPosition(i).toString();
                    array    =   ideskul.split("--");
                    Toast.makeText(getContext(), array[0], Toast.LENGTH_SHORT).show();
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


        return view;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 ){
            fotoPengumuman.setImageURI(data.getData());
            path    =   data.getData();
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
                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EskulModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void buatSpinnerEskul(ArrayList<String> list){
        eskul.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list));
        ((ArrayAdapter)eskul.getAdapter()).notifyDataSetChanged();
    }

    private void insertPengumuman(){
        Retrofit retrofit   =   new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        PengumumanApi api        =       retrofit.create(PengumumanApi.class);
        Call<String> call        =       api.insertPengumuman(judul.getText().toString(), isi.getText().toString(), status.getSelectedItem().toString(), uri_foto, Integer.parseInt(array[0]));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
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
    private void uploadFoto(){
        progressDialog = new ProgressDialog(getContext());
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
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}