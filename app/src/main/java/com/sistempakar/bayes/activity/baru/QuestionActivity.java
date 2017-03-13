package com.sistempakar.bayes.activity.baru;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sistempakar.bayes.R;
import com.sistempakar.bayes.database.DatabaseHelper;
import com.sistempakar.bayes.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by fachrifebrian on 3/13/17.
 */

public class QuestionActivity extends AppCompatActivity {
    public static final int requestCode = 1235;
    public static int questionCount;

    public static HashMap<Integer, Integer> answerMap = new HashMap<>();
    private final HashMap<Integer, String> questionMap = new HashMap<>();
    private final HashMap<Integer, String> questionTypeMap = new HashMap();
    private final HashMap<Integer, String> questionDescMap = new HashMap();

    private final ArrayList alphabetList = new ArrayList();
    private final String[] alphabetStr = new String[]{"A", "B", "C", "D", "E", "F", "G", " H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", " Y", "Z"};

    private DatabaseHelper dbHelper;
    private Cursor answerCursor;

    private String questionDescription;
    private ArrayList<Integer> questionIdArray;

    private int id;
    private int question_id;
    private String question_name;
    private String question_type;
    private int rowIndex = 0;
    private int tempId;
    private int total_question;


    private TextView questionNumText;
    private TextView questionText;
    private RadioGroup radioGroup;
    private Button backQuestionButton;
    private Button nextQuestionBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionNumText = (TextView) findViewById(R.id.questionNumberText);
        questionText = (TextView) findViewById(R.id.questionDisplayText);
        nextQuestionBtn = (Button) findViewById(R.id.nextButtonID);
        backQuestionButton = (Button) findViewById(R.id.backButtonID);
        radioGroup = (RadioGroup) findViewById(R.id.answerRadioGrp);

        dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.addAll(alphabetList, alphabetStr);

        Cursor question = dbHelper.getQuestionIdWithName();
        if (question == null || !question.moveToFirst()) {
            Log.d("question+++", String.valueOf(questionMap.values()));
            questionIdArray = new ArrayList(questionMap.keySet());
            radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
            displayQuestion();
            nextQuestionBtn.setOnClickListener(onNextClickListener);
            backQuestionButton.setOnClickListener(onBackClickListener);
            dbHelper.close();
        }
        do {
            question_id = question.getInt(question.getColumnIndex(DatabaseHelper.KEY_QUESTION_ID));
            question_name = question.getString(question.getColumnIndex(DatabaseHelper.KEY_QUESTION_NAME));
            question_type = question.getString(question.getColumnIndex("type"));
            questionDescription = question.getString(question.getColumnIndex("description"));
            questionMap.put(question_id, question_name);
            questionTypeMap.put(question_id, question_type);
            questionDescMap.put(question_id, questionDescription);

        } while (question.moveToNext());
        Log.d("question+++", String.valueOf(questionMap.values()));

        questionIdArray = new ArrayList(questionMap.keySet());

        displayQuestion();

        nextQuestionBtn.setOnClickListener(onNextClickListener);
        backQuestionButton.setOnClickListener(onBackClickListener);
        dbHelper.close();

    }


    private void displayQuestion() {
        radioGroup.removeAllViews();
        if (questionCount == questionIdArray.size() - 1) {
            nextQuestionBtn.setText("Hasilnya");
        } else {
            nextQuestionBtn.setText("Selanjutnya");
        }

        rowIndex = questionCount;
        id = questionIdArray.get(questionCount);
        questionNumText.setText((questionCount + 1) + ".   ");

        if (questionDescMap.get(id).trim().equalsIgnoreCase("")) {
            questionText.setText(Html.fromHtml(questionMap.get(id)));
            questionText.setOnClickListener(null);
        } else {
            questionText.setText(Html.fromHtml("<u>" + questionMap.get(id) + "</u>"));
            questionText.setOnClickListener(onQuestionClickListener);
        }

        answerCursor = dbHelper.getAnswer(id);
        int ansSize = answerCursor.getCount();
        for (int i = 0; i < ansSize; i++) {
            RadioButton radioButton = new RadioButton(this);

            boolean checkStatus = false;
            int answer_id = answerCursor.getInt(answerCursor.getColumnIndex(DatabaseHelper.KEY_QUESTION_ID));
            int answer_value = answerCursor.getInt(answerCursor.getColumnIndex("indexvalue"));

            radioButton.setId(answer_value);

            if (questionTypeMap.get(id).equalsIgnoreCase("ABCD")) {
                radioButton.setText(alphabetList.get(i) + ".  " + answerCursor.getString(answerCursor.getColumnIndex(DatabaseHelper.KEY_ANSWER)));
            } else {
                radioButton.setText(answerCursor.getString(answerCursor.getColumnIndex(DatabaseHelper.KEY_ANSWER)));
            }

            if (answerMap.containsKey(rowIndex)) {
                tempId = answerMap.get(rowIndex);
                if (tempId == answer_value) {
                    checkStatus = true;
                }
            }

            radioButton.setChecked(checkStatus);

            answerCursor.moveToNext();
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        radioGroup.invalidate();

    }

    private boolean getCheckStatus(RadioGroup grp) {
        for (int i = 0; i < grp.getChildCount(); i++) {
            if (((RadioButton) grp.getChildAt(i)).isChecked()) {
                return true;
            }
        }
        return false;
    }

    public static void setQuestionIndex(int _index) {
        questionCount = _index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultActivity.requestCode) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }

    @Override public boolean onSupportNavigateUp() {
        dialogExit();
        return true;
    }

    private final RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if (radioGroup.getCheckedRadioButtonId() == checkedId) {
                answerMap.put(rowIndex, checkedId);
            }
        }
    };

    private final View.OnClickListener onNextClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (getCheckStatus(radioGroup)) {
                questionCount++;
                if (questionCount < questionIdArray.size()) {
                    displayQuestion();
                } else {
                    Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                    startActivityForResult(intent, ResultActivity.requestCode);
                }
            } else {
                dialogNotAnswer();
            }
        }
    };
    private final View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            if (questionCount > 0) {
                questionCount--;
                displayQuestion();
            } else {
                dialogExit();
            }
        }
    };

    private final View.OnClickListener onQuestionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogQuestion();
        }
    };


    private void dialogQuestion() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle(Html.fromHtml(questionMap.get(id).trim()));
        alertbuilder.setMessage(Html.fromHtml(questionDescMap.get(id).trim()));
        alertbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbuilder.show();
    }

    private void dialogNotAnswer() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Peringatan");
        alertbuilder.setMessage("Jawaban belum dipilih!");
        alertbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbuilder.show();
    }

    private void dialogExit() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setTitle("Peringatan");
        alertbuilder.setMessage("Kembali ke menu sebelumnya ?");
        alertbuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Utility.resetData();
                dialog.dismiss();
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
