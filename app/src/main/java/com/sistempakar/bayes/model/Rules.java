package com.sistempakar.bayes.model;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 12/18/15.
 */
public class Rules implements Serializable {

    public String idRules, namaRules;
    public String pilihRules;


    public Rules(String idRules, String namaRules, String pilihRules) {
        this.idRules = idRules;
        this.namaRules = namaRules;
        this.pilihRules = pilihRules;
    }

    public String getIdRules() {
        return idRules;
    }

    public String getNamaRules() {
        return namaRules;
    }

    public String getPilihRules() {
        return pilihRules;
    }

    public void setPilihRules(String pilihRules) {
        this.pilihRules = pilihRules;
    }

}
