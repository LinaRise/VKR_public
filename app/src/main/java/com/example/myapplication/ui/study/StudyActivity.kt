package com.example.myapplication.ui.study

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.entity.Word
import java.security.AccessController.getContext


class StudyActivity : AppCompatActivity() {
    var wordsDisplayed = ArrayList<Word?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        var progress = arrayListOf<Int>(R.id.point1,R.id.point2,R.id.point3,R.id.point4,R.id.point5,R.id.point6,R.id.point7,R.id.point8,
        R.id.point9,R.id.point10,R.id.point11,R.id.point12,R.id.point13,R.id.point14,R.id.point15,R.id.point16,
            R.id.point17,R.id.point18,R.id.point19,R.id.point20)

        val scale: Float = baseContext.resources.displayMetrics.density
        wordsDisplayed = intent.getParcelableArrayListExtra("wordsDisplayed")
        if (wordsDisplayed.isNotEmpty()) {
            val linearLayout = findViewById<View>(R.id.line1) as LinearLayout
            if (wordsDisplayed.size < 20) {
                for (i in 1..wordsDisplayed.size) {
                    val textView = TextView(this)
                    textView.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f
                    )
                    textView.id = R.id.point1
                    progress.add(i)
                    textView.width = 0
                    textView.height = (10 * scale + 0.5f).toInt()
                    textView.setBackgroundResource(R.drawable.style_points)
                    linearLayout.addView(textView)
                }
            } else {
                for (i in 1..20) {
                    val textView = TextView(this)
                    textView.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f
                    )
                    textView.width = 0
                    textView.height = (10 * scale + 0.5f).toInt()
                    textView.setBackgroundResource(R.drawable.style_points)
                    linearLayout.addView(textView)
                }
            }
        }
    }
}
