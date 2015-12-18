package com.sistempakar.bayes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sistempakar.bayes.R;
import com.sistempakar.bayes.adapter.GejalaAdapter;
import com.sistempakar.bayes.bayes.BayesClassifier;
import com.sistempakar.bayes.bayes.Classifier;
import com.sistempakar.bayes.database.SQLiteHelper;
import com.sistempakar.bayes.model.Gejala;
import com.sistempakar.bayes.model.ModelBayes;
import com.sistempakar.bayes.model.Rules;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GejalaActivity extends AppCompatActivity {
    private static List<ModelBayes> listBayes = new ArrayList<>();
    ArrayList<Rules> arrayListRulesPositive = new ArrayList<>();
    ArrayList<Rules> arrayListRulesNegative = new ArrayList<>();
    String[] positiveText = new String[0];
    String[] negativeText = new String[0];
    private ListView listView;

//    String positive = "pilek lemas flu";
//    String negative = "demam batuk ";

    //    final String[] positiveText = positive.toLowerCase().split("\\s");
//    final String[] negativeText = negative.toLowerCase().split("\\s");
    private Button buttonPrediksi;
    private ArrayList<Gejala> arrayList = new ArrayList<>();
    private GejalaAdapter gejalaAdapter;
    private SQLiteHelper sqLiteHelper = new SQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gejala);
        initUI();
        initAction();


    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.list_gejala);
        buttonPrediksi = (Button) findViewById(R.id.button_prediksi);

    }

    private void initAction() {
        arrayList = sqLiteHelper.showAllGejala();

//        String[] gejala = new String[]{"pilek", "lemas", "flu", "demam", "batuk"};
//
//        for (int i = 0; i < gejala.length; i++) {
//            arrayList.add(new Gejala(i + "", gejala[i], ""));
//        }

        gejalaAdapter = new GejalaAdapter(this, arrayList);
        listView.setAdapter(gejalaAdapter);

        buttonPrediksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gejalanya = "";
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).getPilihGejala().equals("")) {
                        gejalanya += arrayList.get(i).getPilihGejala().toLowerCase() + " ";
                    }
                }
                System.out.println(gejalanya);
                initBayes(gejalanya);
            }
        });

    }

    private void initBayes(String gejalanya) {

        arrayListRulesPositive = sqLiteHelper.showAllRulesByType("positive");
        arrayListRulesNegative = sqLiteHelper.showAllRulesByType("negative");

        positiveText = new String[arrayListRulesPositive.size()];
        negativeText = new String[arrayListRulesNegative.size()];


        System.out.println(positiveText.length + " " + negativeText.length);

        for (int i = 0; i < arrayListRulesPositive.size(); i++) {
            positiveText[i] = arrayListRulesPositive.get(i).getNamaRules().toLowerCase();
        }

        for (int i = 0; i < arrayListRulesNegative.size(); i++) {
            negativeText[i] = arrayListRulesNegative.get(i).getNamaRules().toLowerCase();
        }

        final String[] unknownText1 = gejalanya.toLowerCase().split("\\s");

        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        /*
         * The classifier can learn from classifications that are handed over
         * to the learn methods. Imagin a tokenized text as follows. The tokens
         * are the text's features. The category of the text will either be
         * positive or negative.
         */
        bayes.learn("positive", Arrays.asList(positiveText));

        bayes.learn("negative", Arrays.asList(negativeText));

        /*
         * Now that the classifier has "learned" two classifications, it will
         * be able to classify similar sentences. The classify method returns
         * a Classification Object, that contains the given featureset,
         * classification probability and resulting category.
         */

        System.out.println( // will output "positive"
                bayes.classify(Arrays.asList(unknownText1)).getCategory() + " " +
                        bayes.classify(Arrays.asList(unknownText1)).getProbability() + " " +
                        bayes.classify(Arrays.asList(unknownText1)).getFeatureset() + " " +
                        bayes.classify(Arrays.asList(unknownText1)).toString()
        );

        String hasil = ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1)).toString();

        System.out.println(((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1)));

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ModelBayes>>() {
        }.getType();
        listBayes = gson.fromJson(hasil, listType);

        double positive = 0, negative = 0;

        for (int i = 0; i < listBayes.size(); i++) {

            if (listBayes.get(i).category.equals("positive")) {
                ((TextView) findViewById(R.id.hasilpositive)).setText("hasil positive : " + listBayes.get(i).probability);
                positive = listBayes.get(i).probability;
            }
            if (listBayes.get(i).category.equals("negative")) {
                ((TextView) findViewById(R.id.hasilnegative)).setText("hasil negative : " + listBayes.get(i).probability);
                negative = listBayes.get(i).probability;
            }

        }

        if (gejalanya.equals("")) {
            ((TextView) findViewById(R.id.hasil)).setText("hasil : anda tidak punya penyakit");
        } else if (bayes.classify(Arrays.asList(unknownText1)).getCategory().toLowerCase().equals("positive")) {
            ((TextView) findViewById(R.id.hasil)).setText("hasil : anda punya penyakit");
        } else if (bayes.classify(Arrays.asList(unknownText1)).getCategory().toLowerCase().equals("negative")) {
            ((TextView) findViewById(R.id.hasil)).setText("hasil : anda tidak punya penyakit");
        }


        /*
         * The BayesClassifier extends the abstract Classifier and provides
         * detailed classification results that can be retrieved by calling
         * the classifyDetailed Method.
         *
         * The classification with the highest probability is the resulting
         * classification. The returned List will look like this.
         * [
         *   Classification [
         *     category=negative,
         *     probability=0.0078125,
         *     featureset=[today, is, a, sunny, day]
         *   ],
         *   Classification [
         *     category=positive,
         *     probability=0.0234375,
         *     featureset=[today, is, a, sunny, day]
         *   ]
         * ]
         */
        ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));

        /*
         * Please note, that this particular classifier implementation will
         * "forget" learned classifications after a few learning sessions. The
         * number of learning sessions it will record can be set as follows:
         */
        bayes.setMemoryCapacity(500); // remember the last 500 learned classifications
    }
}
