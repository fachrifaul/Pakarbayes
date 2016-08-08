package com.sistempakar.bayes.model;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 12/18/15.
 */
public class Gejala implements Serializable {

    public String idGejala, namaGejala;
    public String pilihGejala;
    public String codeGejala;


    public Gejala(String idGejala, String namaGejala, String pilihGejala) {
        this.idGejala = idGejala;
        this.namaGejala = namaGejala;
        this.pilihGejala = pilihGejala;
        this.codeGejala = namaGejala.toLowerCase().replace(" ", "");
    }

    public String getIdGejala() {
        return idGejala;
    }

    public String getCodeGejala() {
        return codeGejala = namaGejala.toLowerCase().replace(" ", "");
    }

    public String getNamaGejala() {
        return namaGejala;
    }

    public String getPilihGejala() {
        return pilihGejala;
    }

    public void setPilihGejala(String pilihGejala) {
        this.pilihGejala = pilihGejala;
    }
}
