package com.sistempakar.bayes.util;

import com.sistempakar.bayes.activity.baru.QuestionActivity;

import java.util.LinkedHashMap;

public class Utility {
    public static int totalAgeGroup = 12;
    public static int userAge = 0;
    public static int userSex = 0;

    public static int getAgeIndex(int sexIndex, int age) {
        int returnIndex = 0;
        if (sexIndex == 2) {
            returnIndex = 6;
        }
        if (age >= 10) {
            returnIndex++;
        }
        if (age >= 20) {
            returnIndex++;
        }
        if (age >= 40) {
            returnIndex++;
        }
        if (age >= 55) {
            returnIndex++;
        }
        if (age >= 71) {
            return returnIndex + 1;
        }
        return returnIndex;
    }

    public static void resetData() {
        userAge = 0;
        userSex = 0;
        QuestionActivity.questionCount = 0;
    }

    public static void resetDataAll(){
        resetData();
        QuestionActivity.answerMap = new LinkedHashMap();
        QuestionActivity.answerMap.clear();
    }
}
