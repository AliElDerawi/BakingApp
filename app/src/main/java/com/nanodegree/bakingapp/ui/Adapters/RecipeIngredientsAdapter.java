package com.nanodegree.bakingapp.ui.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.viewHolder> {

    private ArrayList<RecipeIngredientsRequest> mRecipeIngredientsArrayList;

    public RecipeIngredientsAdapter(ArrayList<RecipeIngredientsRequest> mRecipeIngredientsArrayList ){
        this.mRecipeIngredientsArrayList = mRecipeIngredientsArrayList;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recipe_ingredient,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.mRecipeIngredient.setText(mRecipeIngredientsArrayList.get(position).getIngredient());
        holder.mRecipeIngredientQuantity.setText(String.valueOf(mRecipeIngredientsArrayList.get(position).getIngredientQuantity()));
        holder.mRecipeIngredientMeasure.setText(String.valueOf(mRecipeIngredientsArrayList.get(position).getIngredientMeasure()));
    }

    @Override
    public int getItemCount() {
        if (mRecipeIngredientsArrayList != null && mRecipeIngredientsArrayList.size() > 0){
            return mRecipeIngredientsArrayList.size();
        }else
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_ingredient_text_view)
        TextView mRecipeIngredient;
        @BindView(R.id.recipe_ingredient_measure_text_view)
        TextView mRecipeIngredientMeasure;
        @BindView(R.id.recipe_ingredient_quantity_text_view)
        TextView mRecipeIngredientQuantity;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
