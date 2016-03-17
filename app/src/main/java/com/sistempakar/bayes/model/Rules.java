package com.sistempakar.bayes.model;

import java.io.Serializable;

/**
 * Created by fachrifebrian on 12/18/15.
 */
public class Rules implements Serializable {

    public String idRules, namaRules;
    public String pilihRules;
    public String solusiRules;


    public Rules(String idRules, String namaRules, String pilihRules, String solusiRules) {
        this.idRules = idRules;
        this.namaRules = namaRules;
        this.pilihRules = pilihRules;
        this.solusiRules = solusiRules;
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

    public String getSolusiRules() {
        return solusiRules;
    }

    public void setPilihRules(String pilihRules) {
        this.pilihRules = pilihRules;
    }

}
