package com.example.myapplication.ui.setCreate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.myapplication.R
import com.example.myapplication.ui.list.ListPageFragment
import com.example.myapplication.ui.setView.SetViewActivity
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

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
    fun updateRecyclerViewInserted() {
    }

    @Test
    fun showWordInputError() {
        val bundle = Bundle()
        val scenario = launchFragmentInContainer<ListPageFragment>()
//        val scenario = launchFragment<ListPageFragment>()
        onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.edit_set_title)).perform(ViewActions.replaceText("Рассвет"))

        onView(withText("OK"))
        .inRoot(isDialog()) // <---
            .check(matches(isDisplayed()))
            .perform(click())


        onView(ViewMatchers.withId(R.id.word_add_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.fill_in_both_lines)))
    }

    @Test
    fun showUndoDeleteWord() {
    }

    @Test
    fun hideKeyboard() {
    }

    @Test
    fun cleanInputFields() {
    }

    @Test
    fun showSuccessSavedToast() {
    }
}