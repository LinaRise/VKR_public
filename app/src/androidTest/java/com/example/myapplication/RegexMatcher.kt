package com.example.myapplication

import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import java.util.regex.Pattern

class RegexMatcher(private val regex: String) : BoundedMatcher<View, TextView>(TextView::class.java) {
    private val pattern = Pattern.compile(regex)

    override fun describeTo(description: Description?) {
        description?.appendText("Checking the matcher on received view: with pattern=$regex")
    }

    override fun matchesSafely(item: TextView?) =
        item?.text?.let {
            pattern.matcher(it).matches()
        } ?: false
}