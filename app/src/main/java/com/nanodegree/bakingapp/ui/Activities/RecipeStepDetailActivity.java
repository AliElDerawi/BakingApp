package com.nanodegree.bakingapp.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.ui.Fragments.ItemRecipeStepFragment;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPES_STEP;
import static com.nanodegree.bakingapp.model.Contracts.EXTRA_RECIPE_NAME;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String mRecipeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_RECIPES_STEP)){
            RecipeStepsRequest mRecipeStep = Parcels.unwrap(intent.getParcelableExtra(EXTRA_RECIPES_STEP));

            if (intent.hasExtra(EXTRA_RECIPE_NAME)){
                mRecipeName = intent.getStringExtra(EXTRA_RECIPE_NAME);
            }

            getSupportActionBar().setTitle(mRecipeStep.getStepShortDescription());
            ItemRecipeStepFragment itemRecipeStepFragment = new ItemRecipeStepFragment();
            itemRecipeStepFragment.setRecipeStep(mRecipeStep);
            itemRecipeStepFragment.setRecipeName(mRecipeName);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_fragment,itemRecipeStepFragment)
                    .commit();
        }
    }
}
