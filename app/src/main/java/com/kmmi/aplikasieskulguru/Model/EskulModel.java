package com.kmmi.aplikasieskulguru.Model;

public class EskulModel {

    private int id_eskul;
    private String nama_eskul;
    private String bayaran;
    private String jadwal;
    private String info;

    public int getId_eskul() {
        return id_eskul;
    }

    public void setId_eskul(int id_eskul) {
        this.id_eskul = id_eskul;
    }

    public String getNama_eskul() {
        return nama_eskul;
    }

    public void setNama_eskul(String nama_eskul) {
        this.nama_eskul = nama_eskul;
    }

    public String getBayaran() {
        return bayaran;
    }

    public void setBayaran(String bayaran) {
        this.bayaran = bayaran;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
