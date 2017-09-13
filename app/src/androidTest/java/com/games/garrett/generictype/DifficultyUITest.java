package com.games.garrett.generictype;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.internal.runner.junit3.AndroidJUnit3Builder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Garrett on 7/17/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DifficultyUITest {

    private static final String PACKAGE_NAME = "com.games.garrett.generictype";

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void change_difficulty_to_easy_test(){
        onView(withId(R.id.radio_easy)).perform(click());

        onView(withId(R.id.radio_easy))
                .check(matches(isChecked()));

        onView(withId(R.id.radio_normal))
                .check(matches(isNotChecked()));

        onView(withId(R.id.radio_hard))
                .check(matches(isNotChecked()));
    }

    @Test
    public void change_difficulty_to_normal_test(){
        onView(withId(R.id.radio_normal)).perform(click());

        onView(withId(R.id.radio_easy))
                .check(matches(isNotChecked()));

        onView(withId(R.id.radio_normal))
                .check(matches(isChecked()));

        onView(withId(R.id.radio_hard))
                .check(matches(isNotChecked()));
    }

    @Test
    public void change_difficulty_to_hard_test(){
        onView(withId(R.id.radio_hard)).perform(click());

        onView(withId(R.id.radio_easy))
                .check(matches(isNotChecked()));

        onView(withId(R.id.radio_normal))
                .check(matches(isNotChecked()));

        onView(withId(R.id.radio_hard))
                .check(matches(isChecked()));
    }

    @Test
    public void verify_game_one_launch(){


        onView(withId(R.id.game_one_button))
                .perform(click());

        intended(allOf(
                //hasComponent(hasClassName(GameOneActivity.class.getName())),
                hasExtras(hasEntry(equalTo("difficulty"), equalTo(0))),
                toPackage(PACKAGE_NAME)));

    }
}
