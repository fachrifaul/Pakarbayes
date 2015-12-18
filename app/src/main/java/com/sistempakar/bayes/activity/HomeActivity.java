package com.sistempakar.bayes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.database.SQLiteHelper;

public class HomeActivity extends AppCompatActivity {
    private SQLiteHelper sqLiteHelper = new SQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addGejala();

        findViewById(R.id.button_konsultasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GejalaActivity.class));
            }
        });

        findViewById(R.id.button_pakar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BayesActivity.class));
            }
        });

        findViewById(R.id.button_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RulesActivity.class));
            }
        });
    }

    private void addGejala() {
        String[] namaGejala = {"Batuk", "Pilek"};
        String[] jenisGejala = {"positive", "negative"};
        for (int i = 0; i < namaGejala.length; i++) {
            if(!sqLiteHelper.checkIfExist(namaGejala[i])){
                sqLiteHelper.addRules(namaGejala[i], jenisGejala[i]);
            }
        }
    }
}
