package com.sistempakar.bayes.activity.baru;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.database.DatabaseHelper;
import com.sistempakar.bayes.util.Utility;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by fachrifebrian on 3/13/17.
 */

public class ResultActivity extends AppCompatActivity {
    public static final int requestCode = 1234;
    private double[][] code;
    private String[][] diseasesArray;
    double[] dsd;
    private int[] num;
    private double[][] prev;
    private double[] prob;

    private double sum = 0.0d;
    private double[] sympproblem;
    private int totalDiseases = 0;
    private int totalQuestion = 0;

    private Button exitButton;
    private Button startAgainButton;

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startAgainButton = (Button) findViewById(R.id.startAgainBtn);
        exitButton = (Button) findViewById(R.id.exitBtn);

        startAgainButton.setOnClickListener(onStartClickListener);
        exitButton.setOnClickListener(onExitClickListener);

        String displayText = "";
        totalQuestion = dbHelper.getTotalQuestion();
        totalDiseases = dbHelper.getTotalDiseases();
        int maximumIndex = dbHelper.getMaximumIndexValue();
        sympproblem = new double[totalDiseases];
        dsd = new double[totalDiseases];
        prob = new double[totalDiseases];
        num = new int[totalDiseases];
        diseasesArray = (String[][]) Array.newInstance(String.class, totalDiseases, 2);
        code = (double[][]) Array.newInstance(Double.TYPE, totalDiseases, maximumIndex + 1);
        for (int i = 0; i < totalDiseases; i++) {
            int j;
            for (j = 0; j < maximumIndex + 1; j++) {
                code[i][j] = 0.0d;
            }
        }
        generateCodeAnswerArray();
        prev = (double[][]) Array.newInstance(Double.TYPE, totalDiseases, Utility.totalAgeGroup);
        for (int i = 0; i < totalDiseases; i++) {
            for (int j = 0; j < Utility.totalAgeGroup; j++) {
                prev[i][j] = 0.0d;
            }
        }
        generateCodePrevalencesArray();
        generateDiseasesArray();
        calculateDiseases();
    }


    public void calculateDiseases() {
        int i;
        double[] dArr;
        int ageIndex = Utility.getAgeIndex(Utility.userSex, Utility.userAge);

        for (i = 0; i < totalDiseases; i++) {
            sympproblem[i] = 1.0d;
        }
        for (i = 0; i < totalQuestion; i++) {
            int j;

            if (QuestionActivity.answerMap.containsKey(i)) {
                int indexValue = QuestionActivity.answerMap.get(i);

                for (j = 0; j < totalDiseases; j++) {
                    if (indexValue < 2000) {
                        int ref;
                        if (indexValue > 1000) {
                            ref = indexValue - 1000;
                            dArr = sympproblem;
                            dArr[j] = dArr[j] * ((100.0d - code[j][ref]) / 10.0d);
                        } else if (indexValue > 0) {
                            ref = indexValue;
                            dArr = sympproblem;
                            dArr[j] = dArr[j] * (code[j][ref] / 10.0d);
                        }
                    }
                }
            }
        }
        for (i = 0; i < totalDiseases; i++) {
            dsd[i] = prev[i][ageIndex] * sympproblem[i];
            sum += dsd[i];
        }
        if (sum == 0.0d) {
            for (i = 0; i < totalDiseases; i++) {
                prob[i] = prev[i][ageIndex] / 100.0d;
            }
        } else {
            for (i = 0; i < totalDiseases; i++) {
                dArr = prob;
                double[] dArr2 = dsd;
                dArr[i] = dArr2[i] / sum;
            }
        }
        for (i = 0; i < totalDiseases; i++) {
            dArr = prob;
            dArr[i] = dArr[i] * 100.0d;
            if (prob[i] < 9.95d) {
                if (prob[i] < 0.1d) {
                    prob[i] = 0.1d;
                }
                prob[i] = Math.floor((prob[i] * 10.0d) + 0.5d) / 10.0d;
            } else {
                prob[i] = (double) Math.round(prob[i]);
            }
            if (prob[i] == 100.0d) {
                prob[i] = 99.9d;
            }


        }
        for (i = 0; i < totalDiseases; i++) {
            num[i] = i;
        }
        i = 0;
        while (true) {
            if (i >= totalDiseases - 1) {
                break;
            }
            for (int j = i + 1; j < totalDiseases; j++) {
                if (prob[num[j]] > prob[num[i]]) {
                    int tmp = num[i];
                    num[i] = num[j];
                    num[j] = tmp;
                }
            }
            i++;
        }
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        for (i = 0; i < totalDiseases; i++) {
            View childItem = getResultChildItem(this, diseasesArray[num[i]][0], prob[num[i]], num[i]);
            if (i == totalDiseases - 1) {
                childItem.findViewById(R.id.cellSeperatorView).setBackgroundColor(0);
                childItem.invalidate();
            }
            resultLayout.addView(childItem);
        }

        generateLinksArray();
    }

    private View getResultChildItem(Context context, String name, double value, int index) {
        View child = getLayoutInflater().inflate(R.layout.result_child, null);

        double valueString = value;
        if (value >= 9.95d) {
            valueString = ((int) value);
        }

        ((TextView) child.findViewById(R.id.tvItemNameAtResultChild)).setText(name);
        ((TextView) child.findViewById(R.id.tvItemValueAtResultChild)).setText(String.valueOf(valueString));

        LinearLayout lin = (LinearLayout) child.findViewById(R.id.percentageLayout);
        int width = (int) ((value / 100.0d) * ((double) TypedValue.applyDimension(1, 100.0f, getResources().getDisplayMetrics())));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, -1));
        linearLayout.setBackgroundColor(Color.rgb(150, 27, 27));
        lin.addView(linearLayout);
        child.setTag(index);
        child.setOnClickListener(onChildClickListener);
        return child;
    }

    public void generateCodeAnswerArray() {
        Cursor codeCursor = dbHelper.getCodeAnswer();
        int codeSize = codeCursor.getCount();
        for (int i = 0; i < codeSize; i++) {
            int y = (i / totalDiseases) + 1;
            code[i % totalDiseases][y] = codeCursor.getDouble(codeCursor.getColumnIndex("code"));
            codeCursor.moveToNext();
        }
    }

    public void generateCodePrevalencesArray() {
        Cursor prevCursor = dbHelper.getCodePrevalences();
        int codeSize = prevCursor.getCount();
        for (int i = 0; i < codeSize; i++) {
            int y = i % Utility.totalAgeGroup;
            prev[i / Utility.totalAgeGroup][y] = prevCursor.getDouble(prevCursor.getColumnIndex("code"));
            prevCursor.moveToNext();
        }
    }

    public void generateDiseasesArray() {
        Cursor disCursur = dbHelper.getDiseases();
        int disSize = disCursur.getCount();
        for (int i = 0; i < disSize; i++) {
            String disName = disCursur.getString(disCursur.getColumnIndex("name"));
            String disDescription = disCursur.getString(disCursur.getColumnIndex("description"));
            diseasesArray[i][0] = disName;
            diseasesArray[i][1] = disDescription;
            disCursur.moveToNext();
        }
    }

    public void generateLinksArray() {
        Cursor linksCursur = dbHelper.getLinks();
        int linksSize = linksCursur.getCount();
        String[][] linksArray = (String[][]) Array.newInstance(String.class, linksSize, 3);
        for (int i = 0; i < linksSize; i++) {
            String url = linksCursur.getString(linksCursur.getColumnIndex("url"));
            String urltext = linksCursur.getString(linksCursur.getColumnIndex("urltext"));
            String description = linksCursur.getString(linksCursur.getColumnIndex("description"));
            linksArray[i][0] = url;
            linksArray[i][1] = urltext;
            linksArray[i][2] = description;
            linksCursur.moveToNext();
        }
    }

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    @Override public boolean onSupportNavigateUp() {
        back();
        return true;
    }

    private void back() {
        QuestionActivity.setQuestionIndex(totalQuestion - 1);
        finish();
    }

    private final View.OnClickListener onStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utility.resetDataAll();
            finish();
        }
    };

    private final View.OnClickListener onExitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogExit();
        }
    };

    private final View.OnClickListener onChildClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int mIndex = (Integer) v.getTag();
            dialogDisease(mIndex);
        }
    };

    private void dialogDisease(int mIndex) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle(diseasesArray[mIndex][0]);
        alertbuilder.setMessage(Html.fromHtml(diseasesArray[mIndex][1]));
        alertbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertbuilder.show();
    }

    private void dialogExit() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Peringatan");
        alertbuilder.setMessage("Kembali ke menu utama ?");
        alertbuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Utility.resetDataAll();
                setResult(RESULT_OK);
                finish();
            }
        });

        alertbuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbuilder.show();
    }
}
