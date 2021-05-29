package com.example.myapplication.ui.setView

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.myapplication.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test




class SetViewActivityTest {

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<SetViewActivity> = ActivityScenarioRule(
        SetViewActivity::class.java
    )



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
            .putExtra("settId", 1L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(withId(R.id.word_add_button)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.fill_in_both_lines)))
    }

    @Test
    fun displayAddedWord() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetViewActivity::class.java
        )
            .putExtra("settId", "1")
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(withId(R.id.original_input)).perform(replaceText("Рассвет"))
        onView(withId(R.id.translated_input)).perform(replaceText("Dawn"), closeSoftKeyboard())
        onView(withId(R.id.recyclerivew_set_create))

        onView(withId(R.id.word_add_button)).perform(click())
        onView(withId(R.id.recyclerivew_set_create))
            .check(matches(atPosition(0, hasDescendant(withText("Рассвет")))))
        onView(withId(R.id.recyclerivew_set_create))
            .check(matches(atPosition(0, hasDescendant(withText("Dawn")))))
    }

    @Test
    fun showUndoDeleteWord() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetViewActivity::class.java
        )
            .putExtra("settId", 1L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(withId(R.id.original_input)).perform(replaceText("Рассвет"))
        onView(withId(R.id.translated_input)).perform(replaceText("Dawn"), closeSoftKeyboard())
        onView(withId(R.id.recyclerivew_set_create)).perform(swipeLeft())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("\\w+ ${R.string.is_deleted}")))

    }
    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


    @Test
    fun showDialogCopy() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            SetViewActivity::class.java
        )
            .putExtra("settId", 1L)
        var scenario = activityScenarioRule.scenario
        scenario = launchActivity(intent)
        onView(withId(R.id.original_input)).perform(replaceText("Рассвет"))
        onView(withId(R.id.translated_input)).perform(replaceText("Dawn"), closeSoftKeyboard())
        onView(withId(R.id.recyclerivew_set_create)).perform(swipeRight())

    }
}