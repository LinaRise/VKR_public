package com.example.myapplication.ui.study

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.setView.SetViewContract
import com.example.myapplication.ui.setView.SetViewPresenter
import kotlinx.android.synthetic.main.activity_study.*
import java.util.*
import kotlin.collections.ArrayList


class StudyActivity : AppCompatActivity(), SetStudyContract.View {
    private var wrongAnswersCount: Int = 0

    private var rightAnswersCount: Int = 0
    private lateinit var asked: List<Boolean>
    private lateinit var presenter: SetStudyContract.Presenter

    lateinit var frontAnim: AnimatorSet
    lateinit var backAnim: AnimatorSet
    var isFront = true

    var wordsDisplayed = ArrayList<Word>()
    lateinit var listToSelectFrom: List<Word>
    lateinit var newList: List<Word>
    lateinit var questionTV: TextView
    lateinit var answerTV: TextView
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
    private var rightWrong = booleanArrayOf(
        false, false, false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false, false, false
    )
    var currentQuestion = 0
    var rightAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)
        dbhelper = DBHelper(this)

        setPresenter(SetStudyPresenter(this, DependencyInjectorImpl(dbhelper)))



        questionTV = findViewById<TextView>(R.id.question_textView)
        answerTV = findViewById<TextView>(R.id.answer_textView)
        option1B = findViewById<Button>(R.id.option1)
        option2B = findViewById<Button>(R.id.option2)
        option3B = findViewById<Button>(R.id.option3)
        option4B = findViewById<Button>(R.id.option4)


        val scale = applicationContext.resources.displayMetrics.density
        questionTV.cameraDistance = 8000 * scale;
        answerTV.cameraDistance = 8000 * scale;

        frontAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.front_animator
        ) as AnimatorSet
        backAnim = AnimatorInflater.loadAnimator(
            applicationContext,
            R.animator.back_animator
        ) as AnimatorSet

        wordsDisplayed = intent.getParcelableArrayListExtra("wordsDisplayed")
        listToSelectFrom = ArrayList<Word>(wordsDisplayed)
        (listToSelectFrom as ArrayList<Word>).sortBy { it.recallPoint }

        val (newOrForgotten, others) = listToSelectFrom.partition { it.recallPoint < 4 }
        val (average, wellKnown) = others.partition { it.recallPoint < 8 }

        if (wordsDisplayed.isNotEmpty()) {
            linearLayout = findViewById<View>(R.id.progress_icons_lines) as LinearLayout
            if (wordsDisplayed.size < 20) {

                //количество проверяемых слов
                /* val newSize: Int =
                     newOrForgotten.size + (average.size / 3).toInt() + (wellKnown.size / 4).toInt()*/

                //textView для отображения верных и неверных ответов
                for (i in 0 until wordsDisplayed.size) {
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
                val optionsTaken = ArrayList<Word>()
                rightAnswer = (1..4).random()
                Log.d("rightAnswer = ", rightAnswer.toString())
                questionTV.text = newList[0].originalWord
                answerTV.text = newList[0].translatedWord
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
                    answerTV.text = newList[0].translatedWord
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

        option1B.setOnClickListener { setOnClickOnEachButton(1) }
        option2B.setOnClickListener { setOnClickOnEachButton(2) }
        option3B.setOnClickListener { setOnClickOnEachButton(3) }
        option4B.setOnClickListener { setOnClickOnEachButton(4) }
    }


    private fun returnRandomSeries(wordsList: List<Word>, randomSeriesLength: Int): List<Word> {
        wordsList.toMutableList().shuffle()
        return wordsList.subList(0, randomSeriesLength)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    private fun setOnClickOnEachButton(buttonNumber: Int) {
        frontAnim.setTarget(questionTV)
        backAnim.setTarget(answerTV)
        frontAnim.start()
        backAnim.start()

        frontAnim.setTarget(answerTV)
        backAnim.setTarget(questionTV)
        backAnim.start()
        frontAnim.start()

        if (currentQuestion > newList.size - 1) {

        } else {
            if (rightAnswer == buttonNumber) {
                rightWrong[currentQuestion] = true
            }
            for (i in 0 until listToSelectFrom.size) {
                val tv = linearLayout.findViewById<TextView>(progressBarIds[i])
                tv.setBackgroundResource(R.drawable.style_points)
            }
            for (i in 0..currentQuestion) {
                val tv = linearLayout.findViewById<TextView>(progressBarIds[i])
                if (rightWrong[i])
                    tv.setBackgroundResource(R.drawable.style_points_green)
                else
                    tv.setBackgroundResource(R.drawable.style_points_red)
            }
            // присваиваем значение в тексовое поле вопроса
            questionTV.text = newList[currentQuestion].originalWord
            answerTV.text = newList[currentQuestion].translatedWord
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
                Log.d("StudyActivity", Date().toString())

                showSetStudyEnd()

                presenter.updateStudyProgress(
                    StudyProgress(
                        java.time.LocalDate.now(),
                        rightAnswersCount,
                        wrongAnswersCount
                    )
                )
                for ((index, word) in newList.withIndex()) {
                    println("The element at $index is $word")
                    if (asked[index])
                        word.recallPoint = word.recallPoint + 1
                    else {
                        if (word.recallPoint > 1)
                            word.recallPoint = word.recallPoint - 2

                    }
                }
                presenter.updateWordsPoints(newList)

            }


        }
    }

    private fun showSetStudyEnd() {
        val setStudyEnd = SetStudyEnd()
        val args = Bundle()
        asked = rightWrong.take(currentQuestion)
        rightAnswersCount = asked.count { it }
        Log.d("rightAnswers", rightAnswersCount.toString())
        wrongAnswersCount = asked.count { !it }
        Log.d("wrongAnswers", wrongAnswersCount.toString())
        args.putInt("rightAnswers", rightAnswersCount)
        args.putInt("wrongAnswers", wrongAnswersCount)
        setStudyEnd.arguments = args
        val manager = supportFragmentManager
        setStudyEnd.show(manager, "Set study")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        dbhelper.close()

    }

    override fun setPresenter(presenter: SetStudyContract.Presenter) {
        this.presenter = presenter
    }

}

