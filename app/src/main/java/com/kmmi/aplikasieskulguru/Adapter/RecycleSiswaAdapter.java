package com.kmmi.aplikasieskulguru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmmi.aplikasieskulguru.Model.SiswaModel;
import com.kmmi.aplikasieskulguru.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleSiswaAdapter extends RecyclerView.Adapter<RecycleSiswaAdapter.MyViewHolder> {

    List<SiswaModel> listSiswa ;
    Context context;
    LayoutInflater inflater;

    public RecycleSiswaAdapter(List<SiswaModel> listSiswa, Context context) {
        this.listSiswa = listSiswa;
        this.context = context;
        inflater    =   LayoutInflater.from(context);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView namaSiswa;
        CircleImageView fotoSiswa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaSiswa   =   itemView.findViewById(R.id.namaSiswa);
            fotoSiswa   =   itemView.findViewById(R.id.fotoSiswa);
        }
    }

    @NonNull
    @Override
    public RecycleSiswaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = inflater.inflate(R.layout.custom_list_siswa, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleSiswaAdapter.MyViewHolder holder, int position) {

        holder.namaSiswa.setText(listSiswa.get(position).getNama());
        String url = listSiswa.get(position).getFoto_profile();
        Glide.with(context).load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(holder.fotoSiswa);
    }
    @Override
    public int getItemCount() {
        return listSiswa.size();
    }
}
