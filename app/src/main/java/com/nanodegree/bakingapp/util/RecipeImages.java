package com.nanodegree.bakingapp.util;

import com.nanodegree.bakingapp.R;

public class RecipeImages {

    public static int getImageDrawable(String mRecipeName ){
        int mRecipeImage = R.drawable.default_recipe;
        switch (mRecipeName.toLowerCase()) {
            case "nutella pie":
                mRecipeImage = R.drawable.recipe_nutella_pie;
                break;
            case "brownies":
                mRecipeImage = R.drawable.recipe_brownie;
                break;
            case "cheesecake":
                mRecipeImage = R.drawable.recipe_cheese_cake;
                break;
            case "yellow cake":
                mRecipeImage = R.drawable.recipe_yellow_cake;
                break;

            default:
                mRecipeImage = R.drawable.default_recipe;
        }
        return mRecipeImage;
    }
}
