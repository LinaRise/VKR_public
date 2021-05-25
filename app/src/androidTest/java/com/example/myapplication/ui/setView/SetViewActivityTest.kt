package com.example.myapplication.ui.setView

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.myapplication.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SetViewActivityTest {

    @get:Rule var activityScenarioRule = activityScenarioRule<SetViewActivity>()


    @Before
    fun setUp() {
        Intents.init();
        /* Intents.init()
         //обращаемся к базе данных
         var intent = Intent()
         intent.putExtra("SUBJECT_ID", id)
         intent.putExtra("SUBJECT_TITLE", subjectTitle)
         intent.putExtra("SUBJECT_TEACHER", subjectTeacher)
         mActivityRule.launchActivity(intent)*/
    }

    @After
    fun tearDown() {
        Intents.release();
    }

    @Test
    fun showWordInputError() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetViewActivity::class.java
        )
            .putExtra("settId", "1")
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(withId(R.id.word_add_button)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.fill_in_both_lines)))

    }

    @Test
    fun showUndoDeleteWord() {
    }

    @Test
    fun cleanInputFields() {
    }

    @Test
    fun setData() {
    }

    @Test
    fun onInputedData() {
    }

    @Test
    fun showDialog() {
    }
}