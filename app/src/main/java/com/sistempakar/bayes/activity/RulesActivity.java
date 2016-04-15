package com.sistempakar.bayes.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.adapter.RulesAdapter;
import com.sistempakar.bayes.database.SQLiteHelper;
import com.sistempakar.bayes.model.Rules;

public class RulesActivity extends AppCompatActivity {
    private ListView listView;
    private RulesAdapter rulesAdapter;
    private SQLiteHelper sqLiteHelper;

    private String hasilGejala = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sqLiteHelper = new SQLiteHelper(this);

        setContentView(R.layout.activity_rules2);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list_rules);

        setCallBack();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rules, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add:
                showDialogAdd();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setCallBack() {
        rulesAdapter = new RulesAdapter(this, sqLiteHelper.showAllRules());
        listView.setAdapter(rulesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Rules rules = sqLiteHelper.getDataById(Integer.parseInt(sqLiteHelper.showAllRules().get(position).getIdRules()));

                final String[] option = new String[]{"Edit", "Delete"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RulesActivity.this, android.R.layout.select_dialog_item, option);
                AlertDialog.Builder builder = new AlertDialog.Builder(RulesActivity.this);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showDialogEdit(rules);
                        } else if (which == 1) {
                            showDialogDelete(rules);
                        }
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    private void showDialogEdit(Rules rules) {
        AlertDialog.Builder builderEditRules = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_rules, null);
        final TextView idGejala = (TextView) view.findViewById(R.id.id_gejala);
        final EditText editNamaGelaja = (EditText) view.findViewById(R.id.input_nama_gejala);
        final EditText editSolusiGelaja = (EditText) view.findViewById(R.id.input_solusi_gejala);
        final RadioButton radioPositive = (RadioButton) view.findViewById(R.id.radio_positive);
        final RadioButton radioNegative = (RadioButton) view.findViewById(R.id.radio_negative);

        idGejala.setText(rules.getIdRules());
        editNamaGelaja.setText(rules.getNamaRules());
        editSolusiGelaja.setText(rules.getSolusiRules());
        if (rules.getPilihRules().equals("positive")) {
            radioPositive.setChecked(true);
            editSolusiGelaja.setVisibility(View.VISIBLE);
        } else {
            radioNegative.setChecked(true);
            editSolusiGelaja.setVisibility(View.GONE);
        }

        if (radioPositive.isChecked()) {
            hasilGejala = radioPositive.getText().toString();
        } else {
            hasilGejala = radioNegative.getText().toString();
        }

        radioPositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasilGejala = radioPositive.getText().toString();
                    editSolusiGelaja.setVisibility(View.VISIBLE);
                }
            }
        });

        radioNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasilGejala = radioNegative.getText().toString();
                    editSolusiGelaja.setVisibility(View.GONE);
                }
            }
        });

        builderEditRules.setTitle("Update Rules");
        builderEditRules.setView(view);
        builderEditRules.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nama = editNamaGelaja.getText().toString();
                        String hasil = hasilGejala;
                        String solusi = editSolusiGelaja.getText().toString();

                        System.out.println("Nama Gejala : " + nama + " Hasil Gejala : " + hasil);

                        sqLiteHelper.updateRules(
                                Integer.parseInt(idGejala.getText().toString()),
                                nama, hasil, solusi);

                        rulesAdapter.notifyDataSetChanged();

					    /* restart activity */
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(R.anim.do_nothing, R.anim.do_nothing);
                    }

                });

        builderEditRules.setNegativeButton("Kembali",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderEditRules.show();
    }


    private void showDialogDelete(Rules rules) {

        sqLiteHelper.deleteRules(Integer.parseInt(rules.getIdRules()));

		/* restart activity */
        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.do_nothing, R.anim.do_nothing);
    }


    private void showDialogAdd() {
        AlertDialog.Builder builderAddRules = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_rules, null);
        final TextView idGejala = (TextView) view.findViewById(R.id.id_gejala);
        final EditText editNamaGelaja = (EditText) view.findViewById(R.id.input_nama_gejala);
        final EditText editSolusiGelaja = (EditText) view.findViewById(R.id.input_solusi_gejala);
        final RadioButton radioPositive = (RadioButton) view.findViewById(R.id.radio_positive);
        final RadioButton radioNegative = (RadioButton) view.findViewById(R.id.radio_negative);

        if (radioPositive.isChecked()) {
            hasilGejala = radioPositive.getText().toString();
        } else {
            hasilGejala = radioNegative.getText().toString();
        }

        radioPositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasilGejala = radioPositive.getText().toString();
                    editSolusiGelaja.setVisibility(View.VISIBLE);
                }
            }
        });

        radioNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasilGejala = radioNegative.getText().toString();
                    editSolusiGelaja.setVisibility(View.GONE);
                }
            }
        });

        builderAddRules.setTitle("Tambah Rules");
        builderAddRules.setView(view);
        builderAddRules.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nama = editNamaGelaja.getText().toString();
                        String hasil = hasilGejala;
                        String solusi = editSolusiGelaja.getText().toString();


                        System.out.println("Nama Gejala : " + nama + " Hasil Gejala : " + hasil);

                        sqLiteHelper.addRules(nama, hasil, solusi);
                        
					    /* restart activity */
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(R.anim.do_nothing, R.anim.do_nothing);
                    }

                });

        builderAddRules.setNegativeButton("Kembali",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderAddRules.show();
    }

}
