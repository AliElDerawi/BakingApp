package com.nanodegree.bakingapp.ui.Fragments;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.RecipesResult;
import com.nanodegree.bakingapp.ui.Activities.RecipeDetailActivity;
import com.nanodegree.bakingapp.ui.Adapters.MasterRecipeAdapter;
import com.nanodegree.bakingapp.util.MySingleton;
import com.nanodegree.bakingapp.util.OnItemClickListener;
import com.nanodegree.bakingapp.widget.RecipeIngredientWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.Request.Method.GET;
import static com.nanodegree.bakingapp.model.Contracts.CONTENT_URL;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES;
import static com.nanodegree.bakingapp.util.shareMethod.isOnline;
import static com.nanodegree.bakingapp.util.shareMethod.showSnackBar;
import static com.nanodegree.bakingapp.widget.RecipeIngredientWidget.updateRecipeIngredientWidgets;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterRecipeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar mLoadingProgressBar;
    @BindView(R.id.internet_relativeLayout)
    RelativeLayout mInternetLayout;
    @BindView(R.id.btn_reset_connection)
    Button mResetConnectionButton;

    private int position = 0;
    private List<RecipesResult> data = new ArrayList<>();
    private final String TAG = MasterRecipeFragment.class.getSimpleName();


    public MasterRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master_recipe, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.column_span));

        recipeRecyclerView.setLayoutManager(layoutManager);
        mResetConnectionButton.setOnClickListener(this);
        mInternetLayout.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            data = Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_RECIPES));
            if (data != null && data.size() > 0) {
                Log.d(TAG, "onViewStateRestored > " + data.size());
                completeRecyclerView();
            } else {
                requestRecipes();
            }
        } else
            requestRecipes();
    }

    private void requestRecipes() {

        if (isOnline(getContext())) {
            getRecipes();
        } else {
            showSnackBar(getContext(), mLoadingProgressBar, getResources().getString(R.string.no_internet_message));
            mInternetLayout.setVisibility(View.VISIBLE);
        }

    }


    private void completeRecyclerView() {

        MasterRecipeAdapter masterRecipeAdapter = new MasterRecipeAdapter((ArrayList<RecipesResult>) data, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                updateWidget(position);
                Intent forRecipeDetailActivity = new Intent(getContext(), RecipeDetailActivity.class);
                RecipesResult recipesResult = data.get(position);
                forRecipeDetailActivity.putExtra(EXTRA_RECIPES, Parcels.wrap(recipesResult));
                startActivity(forRecipeDetailActivity);
            }
        });

        recipeRecyclerView.setAdapter(masterRecipeAdapter);
        recipeRecyclerView.setHasFixedSize(true);
        ((GridLayoutManager) recipeRecyclerView.getLayoutManager()).scrollToPosition(position);

    }

    private void getRecipes() {

        mLoadingProgressBar.setVisibility(View.VISIBLE);
        String url = CONTENT_URL;
        Log.d(TAG, "getRecipe: url > " + url);

        final StringRequest request = new StringRequest(GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "getRecipe: response > " + response);

                        mLoadingProgressBar.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            data = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<RecipesResult>>() {
                            }.getType());
                            Log.d(TAG, "Your Recipe :" + data.size());

                            if (data.size() > 0) {

                                completeRecyclerView();
                                updateWidget(0);

                            } else {
                                Toast.makeText(getActivity(), getString(R.string.sorry_error_happen),
                                        Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), getString(R.string.sorry_error_happen),
                                    Toast.LENGTH_SHORT).show();
                            mLoadingProgressBar.setVisibility(View.GONE);
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mLoadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.sorry_error_happen),
                        Toast.LENGTH_SHORT).show();
                mInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

    @Override
    public void onClick(View view) {
        if (view == mResetConnectionButton) {
            if (isOnline(view.getContext())) {
                mInternetLayout.setVisibility(View.GONE);
                getRecipes();
            } else {
                showSnackBar(getContext(), view, getResources().getString(R.string.no_internet_message));
            }
        }
    }

    private void updateWidget(int position){
        if (getContext() != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getContext(), RecipeIngredientWidget.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_grid_view);
            RecipeIngredientWidget.mRecipeIngredientsArrayList = data.get(position).getIngredientsRequest();
            RecipeIngredientWidget.mRecipeName = data.get(position).getRecipeName();
            updateRecipeIngredientWidgets(getContext(), appWidgetManager, appWidgetIds);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (data != null && data.size() > 0) {
            outState.putParcelable(EXTRA_RECIPES, Parcels.wrap(data));
            Log.d(TAG, "onSaveInstanceState > " + data.size());
        }
    }
}
