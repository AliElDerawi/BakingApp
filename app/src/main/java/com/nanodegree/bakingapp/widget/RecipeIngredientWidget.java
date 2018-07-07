package com.nanodegree.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;
import com.nanodegree.bakingapp.ui.Activities.HomeActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidget extends AppWidgetProvider {

    public static ArrayList<RecipeIngredientsRequest> mRecipeIngredientsArrayList;
    public static String mRecipeName;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        TODO use GridView and Service instead of Static variables

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_widget);
        if (mRecipeIngredientsArrayList != null) {
            views.setViewVisibility(R.id.no_ingredients_text_view, View.GONE);
            views.setViewVisibility(R.id.recipe_name_text_view,View.VISIBLE);
            views.setViewVisibility(R.id.recipe_ingredient_text_view,View.VISIBLE);
            views.setTextViewText(R.id.recipe_name_text_view,mRecipeName);
            StringBuilder mStringBuilder = new StringBuilder();
            for (RecipeIngredientsRequest recipeIngredientsRequest : mRecipeIngredientsArrayList) {
                mStringBuilder.append("(" + recipeIngredientsRequest.getIngredientQuantity() + " " + recipeIngredientsRequest.getIngredientMeasure() + ")" + "  " + recipeIngredientsRequest.getIngredient());
                mStringBuilder.append("\n");
            }
            views.setTextViewText(R.id.recipe_ingredients_text_view, mStringBuilder);
        } else {
            views.setViewVisibility(R.id.no_ingredients_text_view, View.VISIBLE);
            views.setViewVisibility(R.id.recipe_name_text_view,View.GONE);
            views.setViewVisibility(R.id.recipe_ingredient_text_view,View.GONE);
        }

        views.setTextViewText(R.id.recipe_name_text_view, mRecipeName);

        Intent forHomeActivityIntent = new Intent(context, HomeActivity.class);

        PendingIntent forHomeActivityPendingIntent = PendingIntent.getActivity(context,0,forHomeActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_layout,forHomeActivityPendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateRecipeIngredientWidgets(Context context, AppWidgetManager appWidgetManager,
                                                     int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

