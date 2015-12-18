package com.sistempakar.bayes.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author fachrifaul - fachripaul@gmail.com
 */
public class ModelBayes {
    @SerializedName("category")
    public String category;

    @SerializedName("probability")
    public double probability;

    @SerializedName("featureset")
    public String featureset;
}
