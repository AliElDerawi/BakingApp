package com.nanodegree.bakingapp.ui.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import static com.nanodegree.bakingapp.util.shareMethod.showSnackBar;

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
    @BindView(R.id.fab)
    FloatingActionButton mFabButton;
    @BindView(R.id.recipe_detail_layout)
    NestedScrollView mNestedScrollView;


    private RecipesResult data;
    private boolean mTabletLayout;
    private int mCurrentStepPosition = 0;
    private int[] mScrollPosition;

    private final String TAG = RecipeDetailActivity.class.getSimpleName();
    private final String SCROLL_SAVE_STATE = "scrollSaveState";

    private AnimatedVectorDrawable emptyHeart;
    private AnimatedVectorDrawable fillHeart;
    private boolean full = false;

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

        if (savedInstanceState != null) {
            mCurrentStepPosition = savedInstanceState.getInt(EXTRA_STEP_POSITION);
            mScrollPosition = savedInstanceState.getIntArray(SCROLL_SAVE_STATE);
        }

        if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPES)) {
            data = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPES));
            Log.d(TAG, "data>:" + data.toString());

            if (savedInstanceState == null) {

                setupRecipeIngredientsFragment();
                setupRecipeStepsFragment();
            }

            mRecipeNameTextView.setText(data.getRecipeName());
            mRecipeServingTextView.setText(String.valueOf(data.getRecipeServing()));

            if (mScrollPosition != null)
                mNestedScrollView.scrollTo(mScrollPosition[0], mScrollPosition[1]);

            if (!mTabletLayout) {
                prepareScreenTitle();
                int mRecipeImage = RecipeImages.getImageDrawable(data.getRecipeName());
                GlideApp.with(this).load(mRecipeImage).into(mRecipeImageView);
                emptyHeart = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_heart_empty);
                fillHeart = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_heart_fill);
                mFabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSnackBar(view.getContext(), view, getResources().getString(R.string.fab_not_implemented));
                        AnimatedVectorDrawable drawable = full ? emptyHeart : fillHeart;
                        mFabButton.setImageDrawable(drawable);
                        drawable.start();
                        full = !full;
                    }
                });
            } else {
                mFabButton.setVisibility(View.GONE);
                getSupportActionBar().setTitle(data.getRecipeName());
                mToolbar.setTitle(data.getRecipeName());
                if (savedInstanceState == null) {
                    setupItemRecipeStepFragment();
                }
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
        mCurrentStepPosition = position;
        if (!mTabletLayout) {
            Intent forStepDetailIntent = new Intent(this, RecipeStepDetailActivity.class);
            forStepDetailIntent.putExtra(EXTRA_RECIPES_STEP, Parcels.wrap(data.getStepsRequest()));
            forStepDetailIntent.putExtra(EXTRA_RECIPE_NAME, data.getRecipeName());
            forStepDetailIntent.putExtra(EXTRA_STEP_POSITION, mCurrentStepPosition);
            startActivity(forStepDetailIntent);
        } else {
            setupItemRecipeStepFragment();
        }
    }

    private void setupItemRecipeStepFragment(){
        ItemRecipeStepFragment itemRecipeStepFragment = new ItemRecipeStepFragment();
        itemRecipeStepFragment.setRecipeStepsArrayList(data.getStepsRequest());
        itemRecipeStepFragment.setRecipeStepPosition(mCurrentStepPosition);
        itemRecipeStepFragment.setRecipeName(data.getRecipeName());
        replaceFragment(itemRecipeStepFragment);

    }

    private void setupRecipeIngredientsFragment() {

        ArrayList<RecipeIngredientsRequest> mRecipeIngredientArrayList = data.getIngredientsRequest();
        RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
        recipeIngredientsFragment.setRecipeIngredientsArrayList(mRecipeIngredientArrayList);
        replaceFragment(recipeIngredientsFragment);

    }

    private void setupRecipeStepsFragment() {

        ArrayList<RecipeStepsRequest> mRecipeStepsArrayList = data.getStepsRequest();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeStepsFragment.setStepRecipeStepArrayList(mRecipeStepsArrayList);
        replaceFragment(recipeStepsFragment);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_steps_fragment, fragment).commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentStepPosition != 0) {
            outState.putInt(EXTRA_STEP_POSITION, mCurrentStepPosition);
        }
        outState.putIntArray(SCROLL_SAVE_STATE,
                new int[]{mNestedScrollView.getScrollX(),
                        mNestedScrollView.getScrollY()});

        Log.d(TAG, "onSaveInstanceState" + mNestedScrollView.getScrollX() + " " + mNestedScrollView.getScrollY());
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }
}
