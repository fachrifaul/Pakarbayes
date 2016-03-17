package com.sistempakar.bayes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sistempakar.bayes.R;

public class HasilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        double hasil_positive = bundle.getDouble("hasil_positive");
        double hasil_negative = bundle.getDouble("hasil_negative");
        boolean hasil = bundle.getBoolean("hasil");
        String solusi = bundle.getString("solusi");


        TextView textViewHasilPositive = (TextView) findViewById(R.id.hasilpositive);
        TextView textViewHasilNegative = (TextView) findViewById(R.id.hasilnegative);
        TextView textViewHasil = (TextView) findViewById(R.id.hasil);
        TextView textViewSolusi = (TextView) findViewById(R.id.solusi);


        textViewHasilPositive.setText("Hasil Positif : " + hasil_positive);
        textViewHasilNegative.setText("Hasil Negatif : " + hasil_negative);
        if (hasil) {
            textViewHasil.setText("anda punya penyakit");
            textViewSolusi.setVisibility(View.VISIBLE);
            textViewSolusi.setText("Solusi: \n" + solusi);
        } else {
            textViewHasil.setText("anda tidak punya penyakit");
            textViewSolusi.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
