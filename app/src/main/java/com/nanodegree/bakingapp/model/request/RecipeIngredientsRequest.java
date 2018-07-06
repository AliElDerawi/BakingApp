package com.nanodegree.bakingapp.model.request;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RecipeIngredientsRequest {

    @SerializedName("quantity")
    double ingredientQuantity;
    @SerializedName("measure")
    String ingredientMeasure;
    @SerializedName("ingredient")
    String ingredient;


    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
