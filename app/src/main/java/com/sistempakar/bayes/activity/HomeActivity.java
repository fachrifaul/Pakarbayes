package com.sistempakar.bayes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.activity.baru.InfoActivity;
import com.sistempakar.bayes.database.SQLiteHelper;

public class HomeActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.setupDb();

//        addGejala();

        findViewById(R.id.button_konsultasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InfoActivity.class));
            }
        });

        findViewById(R.id.button_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RulesActivity.class));
            }
        });
        findViewById(R.id.button_tentang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TentangActivity.class));
            }
        });
        findViewById(R.id.button_bantuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BantuanActivity.class));
            }
        });


    }

//    private void addGejala() {
//        String[] namaGejala = {"Batuk", "Pilek"};
//        String[] jenisGejala = {"positive", "negative"};
//        String[] solusiGejala = {"Memberi Aspirin", null};
//        for (int i = 0; i < namaGejala.length; i++) {
//            if(!sqLiteHelper.checkIfExist(namaGejala[i])){
//                sqLiteHelper.addRules(namaGejala[i], jenisGejala[i],solusiGejala[i]);
//            }
//        }
//    }
}
