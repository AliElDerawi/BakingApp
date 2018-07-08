package com.nanodegree.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nanodegree.bakingapp.ui.Activities.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
//    private static final int ITEM_BELOW_THE_FOLD = 1;
    private final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<HomeActivity> mHomeActivityActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mHomeActivityActivityTestRule.getActivity().getIdlingResources();;
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void clickOnRecyclerView_CheckTitle(){

        onView(withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECIPE_NAME)), click()));

        onView(withId(R.id.recipe_name_text_view)).check(matches(withText(RECIPE_NAME)));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null)
            IdlingRegistry.getInstance().unregister(mIdlingResource);
    }


}
