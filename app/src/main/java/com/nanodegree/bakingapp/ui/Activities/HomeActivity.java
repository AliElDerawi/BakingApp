package com.nanodegree.bakingapp.ui.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.bakingapp.IdlingResource.SimpleIdlingResource;
import com.nanodegree.bakingapp.R;
import com.squareup.leakcanary.LeakCanary;

public class HomeActivity extends AppCompatActivity {


    @Nullable
    public static SimpleIdlingResource mSimpleIdlingResource = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getIdlingResources();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
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
