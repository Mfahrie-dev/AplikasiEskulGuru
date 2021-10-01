package com.kmmi.aplikasieskulguru.Model;

public class AdminModel {

    private int id;
    private String username;
    private String password;
    private String nama;
    private String fotoAdmin;
    private int level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFotoAdmin() {
        return fotoAdmin;
    }

    public void setFotoAdmin(String fotoAdmin) {
        this.fotoAdmin = fotoAdmin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
