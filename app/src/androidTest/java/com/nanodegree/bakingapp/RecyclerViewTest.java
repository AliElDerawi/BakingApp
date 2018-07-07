package com.nanodegree.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nanodegree.bakingapp.ui.Activities.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecyclerViewTest {
    private static final int ITEM_BELOW_THE_FOLD = 1;
    private final String itemElementText = "Brownies";

    @Rule
    public ActivityTestRule<HomeActivity> mHomeActivityActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void scrollToItem_checkItsText(){
        onView(ViewMatchers.withId(R.id.recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD, click()));
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }
}
