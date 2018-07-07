package com.nanodegree.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPE_INGREDIENTS;

public class GridWidgetService extends RemoteViewsService {

    private ArrayList<RecipeIngredientsRequest> mRecipeIngredientsRequest;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent.hasExtra(EXTRA_RECIPE_INGREDIENTS)){
            mRecipeIngredientsRequest = Parcels.unwrap(intent.getParcelableExtra(EXTRA_RECIPE_INGREDIENTS));
        }
        return new GridRemoteViewsFactory(this.getApplicationContext(),mRecipeIngredientsRequest);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;
        private ArrayList<RecipeIngredientsRequest> mRecipeIngredientsRequest;

        public GridRemoteViewsFactory(Context mContext,ArrayList<RecipeIngredientsRequest> mRecipeIngredientsRequest) {
            this.mRecipeIngredientsRequest = mRecipeIngredientsRequest;
            this.mContext = mContext;
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mRecipeIngredientsRequest != null && mRecipeIngredientsRequest.size() > 0)
                return mRecipeIngredientsRequest.size();
            else
                return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mRecipeIngredientsRequest != null && mRecipeIngredientsRequest.size() > 0) {
                String mIngredient =  mRecipeIngredientsRequest.get(position).getIngredient();
                String mIngredientQuantity = String.valueOf(mRecipeIngredientsRequest.get(position).getIngredientQuantity());
                String mIngredientMeasure = mRecipeIngredientsRequest.get(position).getIngredientMeasure();

                RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_ingredient_widget);
                views.setTextViewText(R.id.recipe_ingredient_text_view,mIngredient);
                views.setTextViewText(R.id.recipe_ingredient_measure_text_view,mIngredientMeasure);
                views.setTextViewText(R.id.recipe_ingredient_quantity_text_view,mIngredientQuantity);

                return views;


            } else
                return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }


}
