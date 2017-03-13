package com.sistempakar.bayes.activity.lama;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sistempakar.bayes.R;

public class HasilActivity extends AppCompatActivity {
    private double hasil_positive;
    private double hasil_negative;
    private boolean hasil;
    private String solusi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        hasil_positive = bundle.getDouble("hasil_positive");
        hasil_negative = bundle.getDouble("hasil_negative");
        hasil = bundle.getBoolean("hasil");
        solusi = bundle.getString("solusi");


        TextView textViewHasilPositive = (TextView) findViewById(R.id.hasilpositive);
        TextView textViewHasilNegative = (TextView) findViewById(R.id.hasilnegative);
        TextView textViewHasil = (TextView) findViewById(R.id.hasil);
        TextView textViewSolusi = (TextView) findViewById(R.id.solusi);
        ImageView imageviewHasil = (ImageView) findViewById(R.id.imageview_hasil);


        textViewHasilPositive.setText(hasil_positive + "");
        textViewHasilNegative.setText(hasil_negative + "");
        if (hasil) {
            textViewHasil.setText("Anda Punya Penyakit");
            textViewSolusi.setVisibility(View.VISIBLE);
            textViewSolusi.setText(solusi);
            imageviewHasil.setImageResource(R.drawable.ic_correct);
        } else {
            textViewHasil.setText("Anda Tidak Punya Penyakit");
            textViewSolusi.setVisibility(View.GONE);
            imageviewHasil.setImageResource(R.drawable.ic_wrong);
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
