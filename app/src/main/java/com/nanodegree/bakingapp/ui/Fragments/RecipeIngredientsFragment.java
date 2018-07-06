package com.nanodegree.bakingapp.ui.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;
import com.nanodegree.bakingapp.ui.Adapters.RecipeIngredientsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {

    final String TAG  = RecipeIngredientsFragment.class.getSimpleName();

    @BindView(R.id.recipe_ingredients_recycler_view)
    RecyclerView mRecipeIngredientsRecyclerView;

    private ArrayList<RecipeIngredientsRequest> mRecipeIngredientArrayList;


    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        mRecipeIngredientsRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipeIngredientsRecyclerView.setHasFixedSize(true);
        RecipeIngredientsAdapter recipeIngredientsAdapter = new RecipeIngredientsAdapter(mRecipeIngredientArrayList);
        mRecipeIngredientsRecyclerView.setAdapter(recipeIngredientsAdapter);
    }

    public void setRecipeIngredientsArrayList(ArrayList<RecipeIngredientsRequest> mRecipeIngredientsArrayList){
        this.mRecipeIngredientArrayList = mRecipeIngredientsArrayList;
    }
}
