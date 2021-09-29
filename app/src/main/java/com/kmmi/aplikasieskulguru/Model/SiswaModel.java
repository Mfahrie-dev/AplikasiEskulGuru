package com.kmmi.aplikasieskulguru.Model;

public class SiswaModel {

    private int nisn;
    private String nama;
    private String password;
    private String email;
    private String foto_profile;

    public int getNisn() {
        return nisn;
    }

    public void setNisn(int nisn) {
        this.nisn = nisn;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto_profile() {
        return foto_profile;
    }

    public void setFoto_profile(String foto_profile) {
        this.foto_profile = foto_profile;
    }
}
