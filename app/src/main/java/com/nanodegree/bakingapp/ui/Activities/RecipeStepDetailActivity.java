package com.nanodegree.bakingapp.ui.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.ui.Fragments.ItemRecipeStepFragment;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES_STEP;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPE_NAME;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_STEP_POSITION;
import static com.nanodegree.bakingapp.util.shareMethod.showSnackBar;

public class RecipeStepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    RelativeLayout mToolbarLayout;
    @BindView(R.id.navigation_layout)
    RelativeLayout mNavigationLayout;
    @BindView(R.id.previous_step_layout)
    LinearLayout mPreviousStepLayout;
    @BindView(R.id.next_step_layout)
    LinearLayout mNextStepLayout;
    private String mRecipeName = "";
    private int mCurrentStepPosition;
    private ArrayList<RecipeStepsRequest> mRecipeStepsArrayList;
    private final String TAG = RecipeStepDetailActivity.class
            .getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareLandFullScreen();


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_RECIPES_STEP)) {
            mRecipeStepsArrayList = Parcels.unwrap(intent.getParcelableExtra(EXTRA_RECIPES_STEP));
            Log.d(TAG,"data>" + mRecipeStepsArrayList.size());

            if (intent.hasExtra(EXTRA_RECIPE_NAME)) {
                mRecipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
            }

            if (intent.hasExtra(EXTRA_STEP_POSITION)) {
                mCurrentStepPosition = intent.getIntExtra(EXTRA_STEP_POSITION, 0);
            }

            if (savedInstanceState != null){
                mCurrentStepPosition = savedInstanceState.getInt(EXTRA_STEP_POSITION,0);

            }

            getSupportActionBar().setTitle(mRecipeStepsArrayList.get(mCurrentStepPosition).getStepShortDescription());


            if (savedInstanceState == null) {

                Log.d(TAG,"savedInstanceState");
                setupItemRecipeStepFragment();
            }
        }
    }

    private void prepareLandFullScreen() {

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mToolbarLayout.setVisibility(View.VISIBLE);
            mNavigationLayout.setVisibility(View.VISIBLE);
            mNextStepLayout.setOnClickListener(this);
            mPreviousStepLayout.setOnClickListener(this);
        } else {
            mToolbarLayout.setVisibility(View.GONE);
            mNavigationLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == mNextStepLayout) {
            if (mRecipeStepsArrayList != null && mCurrentStepPosition != mRecipeStepsArrayList.size() - 1) {
                stepNavigate(1);
            } else {
                showSnackBar(view.getContext(), view, "You've reached final Step ~");
            }
        }
        if (view == mPreviousStepLayout) {

            if (mRecipeStepsArrayList != null && mCurrentStepPosition != 0) {
                stepNavigate(-1);
            } else {
                showSnackBar(view.getContext(), view, "You're at the first Step ~");
            }
        }
    }

    private void stepNavigate(int step) {

        mCurrentStepPosition = mCurrentStepPosition + step;
        getSupportActionBar().setTitle(mRecipeStepsArrayList.get(mCurrentStepPosition).getStepShortDescription());
       setupItemRecipeStepFragment();
    }

    private void setupItemRecipeStepFragment(){
        ItemRecipeStepFragment itemRecipeStepFragment = new ItemRecipeStepFragment();
        itemRecipeStepFragment.setRecipeStepsArrayList(mRecipeStepsArrayList);
        itemRecipeStepFragment.setRecipeStepPosition(mCurrentStepPosition);
        itemRecipeStepFragment.setRecipeName(mRecipeName);
        replaceFragment(itemRecipeStepFragment);
    }



    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_steps_fragment, fragment).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentStepPosition != 0)
        outState.putInt(EXTRA_STEP_POSITION, mCurrentStepPosition);
    }
}
