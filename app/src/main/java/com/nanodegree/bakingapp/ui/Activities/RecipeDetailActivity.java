package com.nanodegree.bakingapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.RecipesResult;
import com.nanodegree.bakingapp.model.request.RecipeIngredientsRequest;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.ui.Fragments.ItemRecipeStepFragment;
import com.nanodegree.bakingapp.ui.Fragments.RecipeIngredientsFragment;
import com.nanodegree.bakingapp.ui.Fragments.RecipeStepsFragment;
import com.nanodegree.bakingapp.util.GlideApp;
import com.nanodegree.bakingapp.util.RecipeImages;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES_STEP;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPE_NAME;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_STEP_POSITION;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsFragment.OnRecipeSelectedListener {

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.recipe_image_view)
    ImageView mRecipeImageView;
    @BindView(R.id.recipe_servings_text_view)
    TextView mRecipeServingTextView;
    @BindView(R.id.recipe_name_text_view)
    TextView mRecipeNameTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private RecipesResult data;
    private boolean mTabletLayout;
    int mCurrentStepPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.my_tablet_layout) != null) {
            mTabletLayout = true;
        }

        if (savedInstanceState != null){
            mCurrentStepPosition = savedInstanceState.getInt(EXTRA_STEP_POSITION);
        }

        if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPES)) {
            data = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPES));

            ArrayList<RecipeIngredientsRequest> mRecipeIngredientArrayList = data.getIngredientsRequest();
            RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
            recipeIngredientsFragment.setRecipeIngredientsArrayList(mRecipeIngredientArrayList);
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_ingredients_fragment, recipeIngredientsFragment).commit();

            ArrayList<RecipeStepsRequest> mRecipeStepsArrayList = data.getStepsRequest();
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setStepRecipeStepArrayList(mRecipeStepsArrayList);
            getSupportFragmentManager().beginTransaction().add(R.id.recipe_steps_fragment, recipeStepsFragment).commit();

            mRecipeNameTextView.setText(data.getRecipeName());
            mRecipeServingTextView.setText(String.valueOf(data.getRecipeServing()));

            if (!mTabletLayout) {
                prepareScreenTitle();
                int mRecipeImage = RecipeImages.getImageDrawable(data.getRecipeName());
                GlideApp.with(this).load(mRecipeImage).into(mRecipeImageView);
            } else {
                mCollapsingToolbarLayout.setTitle(data.getRecipeName());
                ItemRecipeStepFragment itemRecipeStepFragment = new ItemRecipeStepFragment();
                itemRecipeStepFragment.setRecipeStep(data.getStepsRequest().get(mCurrentStepPosition));
                itemRecipeStepFragment.setRecipeName(data.getRecipeName());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_step_detail_fragment, itemRecipeStepFragment)
                        .commit();
            }

        }
    }

    private void prepareScreenTitle() {

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(data.getRecipeName());
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

    }

    @Override
    public void onRecipeSelected(int position) {
        if (!mTabletLayout) {
            Intent forStepDetailIntent = new Intent(this, RecipeStepDetailActivity.class);
            forStepDetailIntent.putExtra(EXTRA_RECIPES_STEP, Parcels.wrap(data.getStepsRequest().get(position)));
            forStepDetailIntent.putExtra(EXTRA_RECIPE_NAME, data.getRecipeName());
            startActivity(forStepDetailIntent);
        }else {
            mCurrentStepPosition = position;
            ItemRecipeStepFragment itemRecipeStepFragment = new ItemRecipeStepFragment();
            itemRecipeStepFragment.setRecipeStep(data.getStepsRequest().get(position));
            itemRecipeStepFragment.setRecipeName(data.getRecipeName());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment, itemRecipeStepFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentStepPosition != 0){
            outState.putInt(EXTRA_STEP_POSITION,mCurrentStepPosition);
        }
    }
}
