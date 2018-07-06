package com.nanodegree.bakingapp.model;

import com.google.gson.annotations.SerializedName;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class RecipesResult {

    @SerializedName("id")
    int recipeId;
    @SerializedName("name")
    String recipeName;
    @SerializedName("ingredients")
    ArrayList<RecipeIngredientsRequest> ingredientsRequest = null;
    @SerializedName("steps")
    ArrayList<RecipeStepsRequest> stepsRequest;
    @SerializedName("servings")
    int recipeServing;
    @SerializedName("image")
    String recipeImageUrl;

    public  RecipesResult(){

    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<RecipeIngredientsRequest> getIngredientsRequest() {
        return ingredientsRequest;
    }

    public void setIngredientsRequest(ArrayList<RecipeIngredientsRequest> ingredientsRequest) {
        this.ingredientsRequest = ingredientsRequest;
    }

    public ArrayList<RecipeStepsRequest> getStepsRequest() {
        return stepsRequest;
    }

    public void setStepsRequest(ArrayList<RecipeStepsRequest> stepsRequest) {
        this.stepsRequest = stepsRequest;
    }

    public int getRecipeServing() {
        return recipeServing;
    }

    public void setRecipeServing(int recipeServing) {
        this.recipeServing = recipeServing;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }
}
