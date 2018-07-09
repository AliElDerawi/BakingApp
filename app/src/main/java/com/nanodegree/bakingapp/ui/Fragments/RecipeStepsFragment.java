package com.nanodegree.bakingapp.ui.Fragments;


import android.content.Context;
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
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.ui.Adapters.RecipeStepsAdapter;
import com.nanodegree.bakingapp.util.OnItemClickListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES_STEP;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepsFragment extends Fragment {

    private final String TAG = RecipeStepsFragment.class.getSimpleName();

    @BindView(R.id.recipe_steps_recycler_view)
    RecyclerView mRecipeStepsRecyclerView;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<RecipeStepsRequest> mRecipeStepsArrayList;

    private OnRecipeSelectedListener onRecipeSelectedListener;


    public interface OnRecipeSelectedListener {
        void onRecipeSelected(int position);
    }


    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeSelectedListener) {
            onRecipeSelectedListener = (OnRecipeSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeStepsFragment.OnRecipeSelectedListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null){
            mRecipeStepsArrayList = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_RECIPES_STEP));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecipeStepsRecyclerView.setLayoutManager(linearLayoutManager);
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(mRecipeStepsArrayList, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onRecipeSelectedListener.onRecipeSelected(position);
            }
        });
        mRecipeStepsRecyclerView.setAdapter(recipeStepsAdapter);
        mRecipeStepsRecyclerView.setHasFixedSize(true);

    }

    public void setStepRecipeStepArrayList(ArrayList<RecipeStepsRequest> mRecipeStepsArrayList) {
        this.mRecipeStepsArrayList = mRecipeStepsArrayList;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipeStepsArrayList != null)
            outState.putParcelable(EXTRA_RECIPES_STEP, Parcels.wrap(mRecipeStepsArrayList));

    }
}
