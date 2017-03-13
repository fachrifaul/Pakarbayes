package com.sistempakar.bayes.activity.baru;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.database.DatabaseHelper;
import com.sistempakar.bayes.util.Utility;

import java.io.IOException;

/**
 * Created by fachrifebrian on 3/13/17.
 */

public class InfoActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText ageTextValue;
    private Button backButton;
    private Button nextButton;
    private TextView notesView;
    private Spinner sexSpinner;
    private String[] sexString = new String[]{"Pilih Jenis Kelamin", "Laki-laki", "Perempuan"};


    private final View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private final View.OnClickListener onNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ageTextValue.getText().toString().equalsIgnoreCase("")) {
                dialogAge();
            } else if (sexSpinner.getSelectedItemPosition() == 0) {
                dialogSex();
            } else {
                Utility.userAge = Integer.parseInt(ageTextValue.getText().toString());
                Utility.userSex = sexSpinner.getSelectedItemPosition();

                Intent intent = new Intent(InfoActivity.this, QuestionActivity.class);
                startActivityForResult(intent, QuestionActivity.requestCode);
//                startActivity(intent);
            }
        }
    };


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ageTextValue = (EditText) findViewById(R.id.input_umur);
        sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
        notesView = (TextView) findViewById(R.id.notesText);
        backButton = (Button) findViewById(R.id.backButtonID);
        nextButton = (Button) findViewById(R.id.nextButtonID);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notesView.setText(Html.fromHtml("<p>Anda harus menjawab setiap pertanyaan. (Ada "
                + dbHelper.getTotalQuestion() + " pertanyaan).</p>" +
                "<P>Jika Anda siap untuk melanjutkan, tekan tombol \u201cLanjut\u201d.</P>"));

        nextButton.setOnClickListener(onNextClickListener);
        backButton.setOnClickListener(onBackClickListener);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sexString);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sexSpinner.setAdapter(dataAdapter);
        if (Utility.userSex > 0) {
            sexSpinner.setSelection(Utility.userSex);
        }
        if (Utility.userAge > 0) {
            ageTextValue.setText(String.valueOf(Utility.userAge));
        }
    }

    private void dialogSex() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Peringatan");
        alertbuilder.setMessage("Masukan jenis kelamin anda!");
        alertbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbuilder.show();
    }

    private void dialogAge() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Peringatan");
        alertbuilder.setMessage("Masukan umur anda!");
        alertbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbuilder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QuestionActivity.requestCode) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
