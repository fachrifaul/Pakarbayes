package com.sistempakar.bayes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SeizuresAndEpilepsy";
    private static final String DATABASE_NAME_PATH = "/data/data/com.sistempakar.bayes/databases/";

    private static final String DATABASE_PATH = DATABASE_NAME_PATH + DATABASE_NAME;

    private static final String DATABASE_TABLE1 = "questions";
    private static final String DATABASE_TABLE2 = "answers";
    private static final int DATABASE_VERSION = 1;
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_ANSWER_ID = "id";
    public static final String KEY_QUESTION_ID = "id";
    public static final String KEY_QUESTION_NAME = "question";
    private static final String TAG = "DatabaseHelper";
    private final Context context;
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            CopyFiles();
        }
    }

    private void CopyFiles() {
        try {
            InputStream is = this.context.getAssets().open(DATABASE_NAME);
            File outfile = new File(DATABASE_NAME_PATH, DATABASE_NAME);
            outfile.getParentFile().mkdirs();
            outfile.createNewFile();
            if (is == null) {
                throw new RuntimeException("stream is null");
            }
            FileOutputStream out = new FileOutputStream(outfile);
            byte[] buf = new byte[128];
            while (true) {
                int numread = is.read(buf);
                if (numread <= 0) {
                    is.close();
                    out.close();
                    return;
                }
                out.write(buf, 0, numread);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = this.context.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(DATABASE_PATH);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = myInput.read(buffer);
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            } else {
                return;
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        } catch (SQLiteException ignored) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void openDataBase() throws SQLException {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
    }

    public synchronized void close() {
        if (this.db != null) {
            this.db.close();
        }
        super.close();
    }

    public Cursor getQuestionName(int questionId) {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select question from questions where id='" + questionId + "'", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public Cursor getAnswer(int questionId) {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id ,answer ,indexvalue from answers where questionid='" + questionId + "'", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public int getTotalQuestion() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        int size = this.db.rawQuery("select id from questions", null).getCount();
        this.db.close();
        return size;
    }

    public Cursor getQuestionIdWithName() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,question,type,description from questions", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public Cursor getModuleInfoCur() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,infotext from moduleinfo", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public String getModuleInfoText() {
        String returnString = "";
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,infotext from moduleinfo", null);
        cur.moveToFirst();
        returnString = cur.getString(DATABASE_VERSION);
        this.db.close();
        return returnString;
    }

    public int getTotalDiseases() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        int size = this.db.rawQuery("select id from diseases", null).getCount();
        this.db.close();
        return size;
    }

    public Cursor getDiseases() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,name,description from diseases", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public int getMaximumIndexValue() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select max(indexvalue) from answers", null);
        cur.moveToFirst();
        int value = cur.getInt(0);
        this.db.close();
        return value;
    }

    public Cursor getCodeAnswer() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,code,ind_x,ind_y from code_answer order by id asc", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public Cursor getCodePrevalences() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,code,ind_x,ind_y from code_prevalences order by id asc", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public Cursor getLinks() {
        this.db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, DATABASE_VERSION);
        Cursor cur = this.db.rawQuery("select id,url,urltext,description from links", null);
        cur.moveToFirst();
        this.db.close();
        return cur;
    }

    public void onCreate(SQLiteDatabase arg0) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}