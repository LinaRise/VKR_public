package com.example.myapplication.ui.study

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.word.WordUpdateAsyncTask
import com.example.myapplication.database.repo.word.WordUpdateManyAsyncTask
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setCreate.SetCorrectInfoDialog
import kotlinx.android.synthetic.main.activity_study.*
import kotlin.collections.ArrayList


class StudyActivity : AppCompatActivity() {
    var wordsDisplayed = ArrayList<Word>()
    lateinit var listToSelectFrom: List<Word>
    lateinit var newList: List<Word>
    lateinit var questionTV: TextView
    lateinit var option1B: Button
    lateinit var option2B: Button
    lateinit var option3B: Button
    lateinit var option4B: Button
    lateinit var linearLayout: LinearLayout
    lateinit var dbhelper: DBHelper
    private val progressBarIds = arrayListOf<Int>(
        R.id.point1,
        R.id.point2,
        R.id.point3,
        R.id.point4,
        R.id.point5,
        R.id.point6,
        R.id.point7,
        R.id.point8,
        R.id.point9,
        R.id.point10,
        R.id.point11,
        R.id.point12,
        R.id.point13,
        R.id.point14,
        R.id.point15,
        R.id.point16,
        R.id.point17,
        R.id.point18,
        R.id.point19,
        R.id.point20
    )

    //для отображения правильного и неправильного ответа (зеленый/красный)
    var rightWrong = booleanArrayOf(
        false, false, false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false
    )
    var currentQuestion = 0
    var rightAnswer: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)
        dbhelper = DBHelper(this)

        questionTV = findViewById<TextView>(R.id.question_textview)
        option1B = findViewById<Button>(R.id.option1)
        option2B = findViewById<Button>(R.id.option2)
        option3B = findViewById<Button>(R.id.option3)
        option4B = findViewById<Button>(R.id.option4)


        val scale: Float = baseContext.resources.displayMetrics.density
        wordsDisplayed = intent.getParcelableArrayListExtra("wordsDisplayed")
        listToSelectFrom = ArrayList<Word>(wordsDisplayed)
        (listToSelectFrom as ArrayList<Word>).sortBy { it.recallPoint }

        val (newOrForgotten, others) = listToSelectFrom.partition { it.recallPoint < 4 }
        val (average, wellKnown) = others.partition { it.recallPoint < 8 }



        if (wordsDisplayed.isNotEmpty()) {
            linearLayout = findViewById<View>(R.id.progress_icons_lines) as LinearLayout
            if (wordsDisplayed.size < 20) {

                //количество проверяемых слов
                val newSize: Int =
                    newOrForgotten.size + (average.size / 3).toInt() + (wellKnown.size / 4).toInt()

                for (i in 0 until newSize) {
                    val textView = TextView(this)
                    textView.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f
                    )

                    textView.id = progressBarIds[i]
                    textView.width = 0
                    textView.height = (10 * scale + 0.5f).toInt()
                    textView.setBackgroundResource(R.drawable.style_points)
                    linearLayout.addView(textView)
                }

                newList = ArrayList<Word>()
                (newList as ArrayList<Word>).addAll(newOrForgotten)
                (newList as ArrayList<Word>).addAll(
                    returnRandomSeries(
                        average,
                        (average.size / 3).toInt()
                    )
                )
                (newList as ArrayList<Word>).addAll(
                    returnRandomSeries(
                        others,
                        (average.size / 4).toInt()
                    )
                )
                (newList as ArrayList<Word>).shuffle()
                Log.d("newlist", newList[5].toString())
                Log.d("newlist", newList[4].toString())
                Log.d("newlist", newList[3].toString())
                val optionsTaken = ArrayList<Word>()
                rightAnswer = (1..4).random()
                Log.d("rightAnswer = ", rightAnswer.toString())
                questionTV.text = newList[0].originalWord
                var option: Word
                while (true) {
                    if (rightAnswer != 1) {
                        option = listToSelectFrom.random()
                        if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                            option1.text = option.translatedWord
                            optionsTaken.add(option)
                            break
                        }
                    } else {
                        option1.text = newList[currentQuestion].translatedWord
                        optionsTaken.add(newList[currentQuestion])
                        break
                    }
                }
                while (true) {
                    if (rightAnswer != 2) {
                        option = listToSelectFrom.random()
                        if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                            option2.text = option.translatedWord
                            optionsTaken.add(option)
                            break
                        }
                    } else {
                        option2.text = newList[currentQuestion].translatedWord
                        optionsTaken.add(newList[currentQuestion])
                        break
                    }
                }
                while (true) {
                    if (rightAnswer != 3) {
                        option = listToSelectFrom.random()
                        if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                            option3.text = option.translatedWord
                            optionsTaken.add(option)
                            break
                        }
                    } else {
                        option3.text = newList[currentQuestion].translatedWord
                        optionsTaken.add(newList[currentQuestion])
                        break
                    }
                }
                while (true) {
                    if (rightAnswer != 4) {
                        option = listToSelectFrom.random()
                        if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                            option4.text = option.translatedWord
                            optionsTaken.add(option)
                            break
                        }
                    } else {
                        option4.text = newList[currentQuestion].translatedWord
                        optionsTaken.add(newList[currentQuestion])
                        break
                    }
                } /**/

            } else {
                for (i in 0..19) {
                    val textView = TextView(this)
                    textView.layoutParams = TableLayout.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f
                    )
                    textView.id = progressBarIds[i]
                    textView.width = 0
                    textView.height = (10 * scale + 0.5f).toInt()
                    textView.setBackgroundResource(R.drawable.style_points)
                    linearLayout.addView(textView)
                }

                newList = ArrayList<Word>()
                if (newOrForgotten.size >= 20) {
                    if ((average.size + wellKnown.size) > 10) {
                        (newList as ArrayList<Word>).addAll(
                            returnRandomSeries(
                                newOrForgotten,
                                10
                            )
                        )

                        (newList as ArrayList<Word>).addAll(
                            returnRandomSeries(
                                (average + wellKnown),
                                10
                            )
                        )
                    } else {

                        (newList as ArrayList<Word>).addAll(
                            newOrForgotten
                        )
                    }


                    newList.shuffled()
                    val optionsTaken = ArrayList<Word>()
                    rightAnswer = (1..4).random()
                    Log.d("rightAnswer = ", rightAnswer.toString())
                    questionTV.text = newList[0].originalWord
                    var option: Word
                    while (true) {
                        if (rightAnswer != 1) {
                            option = listToSelectFrom.random()
                            if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                                option1.text = option.translatedWord
                                optionsTaken.add(option)
                                break
                            }
                        } else {
                            option1.text = newList[currentQuestion].translatedWord
                            optionsTaken.add(newList[currentQuestion])
                            break
                        }
                    }
                    while (true) {
                        if (rightAnswer != 2) {
                            option = listToSelectFrom.random()
                            if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                                option2.text = option.translatedWord
                                optionsTaken.add(option)
                                break
                            }
                        } else {
                            option2.text = newList[currentQuestion].translatedWord
                            optionsTaken.add(newList[currentQuestion])
                            break
                        }
                    }
                    while (true) {
                        if (rightAnswer != 3) {
                            option = listToSelectFrom.random()
                            if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                                option3.text = option.translatedWord
                                optionsTaken.add(option)
                                break
                            }
                        } else {
                            option3.text = newList[currentQuestion].translatedWord
                            optionsTaken.add(newList[currentQuestion])
                            break
                        }
                    }
                    while (true) {
                        if (rightAnswer != 4) {
                            option = listToSelectFrom.random()
                            if (option !== newList[currentQuestion] && !optionsTaken.contains(option)) {
                                option4.text = option.translatedWord
                                optionsTaken.add(option)
                                break
                            }
                        } else {
                            option4.text = newList[currentQuestion].translatedWord
                            optionsTaken.add(newList[currentQuestion])
                            break
                        }
                    }

                }
            }

        }

        option1B.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                setOnClickOnEachButton(1)

            }
        })
        option2B.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                setOnClickOnEachButton(2)

            }
        })
        option3B.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                setOnClickOnEachButton(3)

            }
        })
        option4B.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                setOnClickOnEachButton(4)

            }
        })
    }


    private fun returnRandomSeries(wordsList: List<Word>, randomSeriesLength: Int): List<Word> {
        wordsList.toMutableList().shuffle()
        return wordsList.subList(0, randomSeriesLength)
    }

    fun setOnClickOnEachButton(buttonNumber: Int) {
        if (currentQuestion > newList.size - 1) {

        } else {
            if (rightAnswer == buttonNumber) {
                rightWrong[currentQuestion] = true
            }
            for (i in 0 until listToSelectFrom.size) {
                var tv = linearLayout.findViewById<TextView>(progressBarIds[i])
                tv.setBackgroundResource(R.drawable.style_points)
            }
            for (i in 0..currentQuestion) {
                var tv = linearLayout.findViewById<TextView>(progressBarIds[i])
                if (rightWrong[i])
                    tv.setBackgroundResource(R.drawable.style_points_green)
                else
                    tv.setBackgroundResource(R.drawable.style_points_red)
            }
            // присваиваем значение в тексовое поле вопроса
            questionTV.text = newList[currentQuestion].originalWord
            var option: Word
            //выбираем рандомно какой ответ будет праильным
            rightAnswer = (1..4).random()
            Log.d("rightAnswer = ", rightAnswer.toString())
            var optionsTaken = ArrayList<Word>()
            //присваиваем значение в текстовые поля ответов
            while (true) {
                if (rightAnswer != 1) {
                    option = listToSelectFrom.random()
                    if (option !== newList[currentQuestion] && !optionsTaken.contains(
                            option
                        )
                    ) {
                        option1.text = option.translatedWord
                        optionsTaken.add(option)
                        break
                    }
                } else {
                    option1.text = newList[currentQuestion].translatedWord
                    optionsTaken.add(newList[currentQuestion])
                    break
                }
            }
            while (true) {
                if (rightAnswer != 2) {
                    option = listToSelectFrom.random()
                    if (option !== newList[currentQuestion] && !optionsTaken.contains(
                            option
                        )
                    ) {
                        option2.text = option.translatedWord
                        optionsTaken.add(option)
                        break
                    }
                } else {
                    option2.text = newList[currentQuestion].translatedWord
                    optionsTaken.add(newList[currentQuestion])
                    break
                }
            }
            while (true) {
                if (rightAnswer != 3) {
                    option = listToSelectFrom.random()
                    if (option !== newList[currentQuestion] && !optionsTaken.contains(
                            option
                        )
                    ) {
                        option3.text = option.translatedWord
                        optionsTaken.add(option)
                        break
                    }
                } else {
                    option3.text = newList[currentQuestion].translatedWord
                    optionsTaken.add(newList[currentQuestion])
                    break
                }
            }
            while (true) {
                if (rightAnswer != 4) {
                    option = listToSelectFrom.random()
                    if (option !== newList[currentQuestion] && !optionsTaken.contains(
                            option
                        )
                    ) {
                        option4.text = option.translatedWord
                        optionsTaken.add(option)
                        break
                    }
                } else {
                    option4.text = newList[currentQuestion].translatedWord
                    optionsTaken.add(newList[currentQuestion])
                    break
                }
            }
            currentQuestion++
            if (currentQuestion == newList.size) {
                Log.d("here", "here")
                val setStudyEnd = SetStudyEnd()
                val args = Bundle()
                val asked = rightWrong.take(currentQuestion)
                val rightAnswers = asked.count { it }
                val wrongAnswers = asked.count { !it }
                args.putInt("rightAnswers", rightAnswers)
                args.putInt("wrongAnswers", wrongAnswers)
//            args.putParcelableArrayList("newList", newList as ArrayList)
                setStudyEnd.arguments = args
                val manager = supportFragmentManager
                setStudyEnd.show(manager, "Set study")

                for ((index, word) in newList.withIndex()) {
                    println("The element at $index is $word")
                    if (asked[index])
                        word.recallPoint = word.recallPoint + 1
                    else {
                        if (word.recallPoint > 1)
                            word.recallPoint = word.recallPoint - 2

                    }
                }
                WordUpdateManyAsyncTask(dbhelper).execute(newList)


            }


        }
    }

}

