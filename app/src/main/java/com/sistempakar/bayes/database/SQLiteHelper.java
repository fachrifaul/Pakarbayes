package com.sistempakar.bayes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.model.Gejala;
import com.sistempakar.bayes.model.Rules;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String namaTabel = "gejala_epilepsi";
    private static final String tabelID = "id_gejala";
    private static final String tabelNamaGejala = "nama_gejala";
    private static final String tabelJenisGejala = "jenis_gejala";
    private static final String tabelSolusiGejala = "solusi_gejala";

    private static final String query_buat_tabel_gejala_epilepsi =
            "CREATE TABLE IF NOT EXISTS " + namaTabel + "(" + tabelID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + tabelNamaGejala + " TEXT," + tabelJenisGejala + " TEXT,"
                    + tabelSolusiGejala + " TEXT)";
    private static final String query_hapus_tabel_gejala_epilepsi = "DROP TABLE IF EXISTS query_buat_tabel_gejala_epilepsi";

    private static final String dbName = "EpilepsiDB";
    private static int currentVersion = 3;

    private Context context;
    private String dbPath;

    public SQLiteHelper(Context context) {
        super(context, dbName, null, currentVersion);
        this.context = context;
        dbPath = context.getDatabasePath(dbName).toString();
        Log.d("epilepsidb", "Database Init constructor ");
    }


    public void setupDb()	{
        if (!checkDatabase())	{
            copyDatabase();
        }
    }

    public boolean checkDatabase(){
        try	{
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            String sql = SQLiteQueryBuilder.buildQueryString(true, "db_version", new String[]{"version"}
                    , null, null, null, null, null);
            Cursor cur = db.rawQuery(sql, null);
            cur.moveToFirst();
            int version = cur.getInt(0);
            if (version != currentVersion)	{
                Log.d("epilepsidb", "DB expired");
                cur.close();

                return false;
            }
            cur.close();
        }
        catch (SQLiteException e)	{
            Log.d("epilepsidb", "DB not exists");
            return false;
        }
        Log.d("epilepsidb", "DB exists");
        return true;
    }

    public void copyDatabase()	{
        Log.d("epilepsidb", "copy database");
        try {
            File file = new File(context.getDatabasePath(dbName).getParent());
            file.mkdir();

            String zipFilePath = context.getDatabasePath("epilepsidb.zip").toString();
            Log.d("EpilepsiDBku", "ZIP FILE PATH " + zipFilePath);
            ZipInputStream zis = new ZipInputStream(context.getResources().openRawResource(R.raw.epilepsidb));
            zis.getNextEntry();

            OutputStream os = new FileOutputStream(dbPath);
            int byteRead;
            byte[] buf = new byte[1024];
            while ((byteRead = zis.read(buf)) > 0)	{
                os.write(buf,0,byteRead);
            }
            zis.close();
            os.close();

        } catch (IOException e) {

            Log.e("epilepsidb", e.getMessage());
        }

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

    public void addRules(String namaGejala, String jenisGejala, String solusiGejala) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tabelNamaGejala, namaGejala.toLowerCase());
        values.put(tabelJenisGejala, jenisGejala);
        values.put(tabelSolusiGejala, solusiGejala);
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
                arrayListRules.add(new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

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

    public int updateRules(int id, String namaGejala, String jenisGejala, String solusiGejala) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(tabelNamaGejala, namaGejala);
        values.put(tabelJenisGejala, jenisGejala);
        values.put(tabelSolusiGejala, solusiGejala);
        return database.update(namaTabel, values, "id_gejala=" + id, null);
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
                rules = new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            } while (cursor.moveToNext());
        }

        return rules;
    }

    public Rules getDataByNamaGejala(String namaGejala) {
        SQLiteDatabase database = this.getReadableDatabase();

        Rules rules = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + namaTabel + " WHERE nama_gejala='" + namaGejala + "'", null);

        if (cursor.moveToFirst()) {
            do {
                rules = new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            } while (cursor.moveToNext());
        }

        return rules;
    }

    public boolean checkIfExist(String nama_gejala) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(namaTabel, new String[]{tabelID,
                        tabelNamaGejala, tabelJenisGejala, tabelSolusiGejala}, tabelNamaGejala + "=?",
                new String[]{nama_gejala.toLowerCase()}, null, null, null, null);

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
                arrayListRules.add(new Rules(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            } while (cursor.moveToNext());
        }

        return arrayListRules;
    }


}
