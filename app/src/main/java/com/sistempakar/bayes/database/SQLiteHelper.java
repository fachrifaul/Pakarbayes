package com.sistempakar.bayes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sistempakar.bayes.model.Gejala;
import com.sistempakar.bayes.model.Rules;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String namaDatabase = "database_epilepsi.db";
    private static final int versiDatabase = 1;
    private static final String namaTabel = "gejala_epilepsi";
    private static final String tabelID = "id_gejala";
    private static final String tabelNamaGejala = "nama_gejala";
    private static final String tabelJenisGejala = "jenis_gejala";
    private static final String query_buat_tabel_gejala_epilepsi =
            "CREATE TABLE IF NOT EXISTS " + namaTabel + "(" + tabelID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tabelNamaGejala + " TEXT," + tabelJenisGejala + " TEXT)";
    private static final String query_hapus_tabel_gejala_epilepsi = "DROP TABLE IF EXISTS query_buat_tabel_gejala_epilepsi";

    public SQLiteHelper(Context context) {
        super(context, namaDatabase, null, versiDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query_buat_tabel_gejala_epilepsi);
        System.out.println("tabel_gejala sudah dibuat");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int versi_lama, int versi_baru) {
        database.execSQL(query_hapus_tabel_gejala_epilepsi);
        onCreate(database);

    }

    public void addRules(String nama_gejala, String jenis_gejala) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tabelNamaGejala, nama_gejala);
        values.put(tabelJenisGejala, jenis_gejala);
        database.insert(namaTabel, null, values);
        database.close();
    }

    public ArrayList<Rules> showAllRules() {
        SQLiteDatabase database = this.getWritableDatabase();

        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<Rules> arrayListRules = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + namaTabel, null);

        // kursor langsung diarkan ke posisi paling awal data pada namaTabel
        if (cursor.moveToFirst()) {
            do {
                arrayListRules.add(new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2)));

            } while (cursor.moveToNext());
        }

        return arrayListRules;
    }

    public ArrayList<Gejala> showAllGejala() {
        SQLiteDatabase database = this.getWritableDatabase();

        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<Gejala> arrayListGejala = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + namaTabel, null);

        // kursor langsung diarkan ke posisi paling awal data pada namaTabel
        if (cursor.moveToFirst()) {
            do {
                arrayListGejala.add(new Gejala(cursor.getString(0), cursor.getString(1), cursor.getString(2)));

            } while (cursor.moveToNext());
        }

        return arrayListGejala;
    }

    public int updateRules(int id, String nama_gejala, String jenis_gejala) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues recordBiodata = new ContentValues();
        recordBiodata.put(tabelNamaGejala, nama_gejala);
        recordBiodata.put(tabelJenisGejala, jenis_gejala);
        return database.update(namaTabel, recordBiodata, "id_gejala=" + id, null);
    }

    public void deleteRules(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM  " + namaTabel + " WHERE id_gejala='" + id + "'");
        database.close();
    }


    public Rules getDataById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Rules rules = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + namaTabel + " WHERE id_gejala=" + id + "", null);

        if (cursor.moveToFirst()) {
            do {
                rules = new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return rules;
    }

    public boolean checkIfExist(String nama_gejala) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(namaTabel, new String[]{tabelID,
                        tabelNamaGejala, tabelJenisGejala}, tabelNamaGejala + "=?",
                new String[]{nama_gejala}, null, null, null, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<Rules> showAllRulesByType(String jenis_gejala) {
        SQLiteDatabase database = this.getWritableDatabase();

        // deklarasikan sebuah arraylist yang bisa menampung hashmap
        ArrayList<Rules> arrayListRules = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + namaTabel + " WHERE " + tabelJenisGejala + " LIKE '%" + jenis_gejala + "%'", null);

        // kursor langsung diarkan ke posisi paling awal data pada namaTabel
        if (cursor.moveToFirst()) {
            do {
                arrayListRules.add(new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2)));

            } while (cursor.moveToNext());
        }

        return arrayListRules;
    }


}
