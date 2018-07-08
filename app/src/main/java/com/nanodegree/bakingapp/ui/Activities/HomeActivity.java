package com.nanodegree.bakingapp.ui.Activities;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nanodegree.bakingapp.IdlingResource.SimpleIdlingResource;
import com.nanodegree.bakingapp.R;

public class HomeActivity extends AppCompatActivity {


    @Nullable
    public static SimpleIdlingResource mSimpleIdlingResource = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getIdlingResources();
    }

    @VisibleForTesting
    @Nullable
    public IdlingResource getIdlingResources(){
        if (mSimpleIdlingResource == null){
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return  mSimpleIdlingResource;
    }
}
