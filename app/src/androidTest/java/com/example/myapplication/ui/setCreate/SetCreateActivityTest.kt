package com.example.myapplication.ui.setCreate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.myapplication.R
import com.example.myapplication.RegexMatcher
import com.example.myapplication.ui.list.ListPageFragment
import com.example.myapplication.ui.setView.SetViewActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetCreateActivityTest {
    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<SetCreateActivity> = ActivityScenarioRule(
        SetCreateActivity::class.java
    )


    @Before
    fun setUp() {
        Intents.init()

    }

    @After
    fun tearDown() {
        Intents.release()
    }


    @Test
    fun showWordInputError() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetCreateActivity::class.java
        ) .putExtra("settId", 2L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)

        onView(ViewMatchers.withId(R.id.word_add_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.fill_in_both_lines)))
    }

    @Test
    fun showUndoDeleteWord() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetCreateActivity::class.java
        )
            .putExtra("settId", 2L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(ViewMatchers.withId(R.id.original_input)).perform(ViewActions.replaceText("Рассвет"))
        onView(ViewMatchers.withId(R.id.translated_input)).perform(
            ViewActions.replaceText("Dawn"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.word_add_button)).perform(click())
        onView(ViewMatchers.withId(R.id.recyclerivew_set_create)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )
        onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Рассвет удален")))

    }



    @Test
    fun showSuccessSavedToast() {

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetCreateActivity::class.java
        )
            .putExtra("settId", 2L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(ViewMatchers.withId(R.id.original_input)).perform(ViewActions.replaceText("Рассвет"))
        onView(ViewMatchers.withId(R.id.translated_input)).perform(
            ViewActions.replaceText("Dawn"),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.word_add_button)).perform(click())
        onView(ViewMatchers.withId(R.id.recyclerivew_set_create)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )
        onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Рассвет удален")))

    }
}