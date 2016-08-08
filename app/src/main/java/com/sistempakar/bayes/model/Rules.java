package com.sistempakar.bayes.model;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 12/18/15.
 */
public class Rules implements Serializable {

    private String id;
    private String nama;
    private String jenis;
    private String solusi;
    private String code;

    public Rules(String id, String nama, String jenis, String solusi, String code) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.solusi = solusi;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    public String getSolusi() {
        return solusi;
    }

    public String getCode() {
        return code;
    }
}
