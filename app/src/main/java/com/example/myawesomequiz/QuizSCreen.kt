package com.example.android.myquizapp

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myawesomequiz.R
import java.util.*

class QuizSCreen : AppCompatActivity() {

    companion object {
        const val EXTRA_SCORE = "extraScore"
        const val COUNTDOWN_IN_MILLIS: Long = 30000
    }

    /*
    * constants for savedInstanceState
    * */
    private val KEY_SCORE = "keyscore"
    private val KEY_QUES_COUNT = "keyQuesCount"
    private val KEY_MILLIS_LEFT = "keyMillisLeft"
    private val KEY_ANSWERED = "keyAnswered"
    private val KEY_QUES_LIST = "keyQuesList"

    var countDownTimer: CountDownTimer? = null
    lateinit var score_tv: TextView
    lateinit var NoOfquestions: TextView
    lateinit var timer: TextView
    lateinit var question: TextView
    lateinit var radioGroup: RadioGroup
    lateinit var rb1: RadioButton
    lateinit var rb2: RadioButton
    lateinit var rb3: RadioButton
    lateinit var confirm_next: Button
    private var list: ArrayList<Question> = ArrayList()
    private lateinit var textDefaultColor: ColorStateList
    private lateinit var textDefaultColorcd: ColorStateList
    private var score: Int = 0
    private var answered: Boolean = false
    private var questionCounter: Int = 0
    private var totalQuestions = 0
    private lateinit var currQuestion: Question
    private var backPressedTime: Long = 0
    private var timeLeftInMillis: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)

        score_tv = findViewById(R.id.score)
        NoOfquestions = findViewById(R.id.question_no)
        timer = findViewById(R.id.timer)
        question = findViewById(R.id.question_text)
        radioGroup = findViewById(R.id.radio_group)
        rb1 = findViewById(R.id.radio_button1)
        rb2 = findViewById(R.id.radio_button2)
        rb3 = findViewById(R.id.radio_button3)
        confirm_next = findViewById(R.id.confirm_next)
        textDefaultColor = rb1.textColors
        textDefaultColorcd = timer.textColors
        timeLeftInMillis = COUNTDOWN_IN_MILLIS

        if (savedInstanceState == null) {
            val dbHelper = QuizDbHelper(this)
            list = dbHelper.getAllQuestions()
            totalQuestions = list.size
            list.shuffle()
            nextQuestion()
        } else {
            list = savedInstanceState.getParcelableArrayList<Question>(KEY_QUES_LIST)!!
            //list.also { list = it }
            totalQuestions = list.size
            score = savedInstanceState.getInt(KEY_SCORE)
            answered = savedInstanceState.getBoolean(KEY_ANSWERED)
            questionCounter = savedInstanceState.getInt(KEY_QUES_COUNT)
            currQuestion = list[questionCounter - 1]
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT)
            if (!answered) {
                startTimer()
            } else {
                updateCoundownText()
                correctAnswer()
            }
        }

        confirm_next.setOnClickListener {
            if (!answered) {
                if (rb1.isChecked || rb2.isChecked || rb3.isChecked) {
                    checkAnswer()
                } else {
                    Toast.makeText(this, "Please select you answer", Toast.LENGTH_SHORT).show()
                }
            } else
                nextQuestion()
        }
    }

    private fun checkAnswer() {
        answered = true
        /*
        * cancel the timer if answer is selected
        * */
        countDownTimer?.cancel()
        try {
            val selected: RadioButton = findViewById(radioGroup.checkedRadioButtonId)

            val answerNo: Int = radioGroup.indexOfChild(selected) + 1

            if (answerNo == currQuestion.getAnswerNo()) {
                score += 1
                score_tv.text = "Score: ${score.toString()}"
            }
        } catch (e: Exception) {
        }
        correctAnswer()
    }

    private fun correctAnswer() {
        when (currQuestion.getAnswerNo()) {
            1 -> {
                rb1.setTextColor(getColor(R.color.correct_ans))
                question.text = getString(R.string.correct_ans_1)
            }
            2 -> {
                rb2.setTextColor(getColor(R.color.correct_ans))
                question.text = getString(R.string.correct_ans_2)
            }
            3 -> {
                rb3.setTextColor(getColor(R.color.correct_ans))
                question.text = getString(R.string.correct_ans_3)
            }
        }

        if (questionCounter < totalQuestions) {
            confirm_next.text = getString(R.string.next_answer)
        } else
            confirm_next.text = getString(R.string.finish)
    }

    private fun nextQuestion() {
        rb1.setTextColor(textDefaultColor)
        rb2.setTextColor(textDefaultColor)
        rb3.setTextColor(textDefaultColor)
        radioGroup.clearCheck()

        if (questionCounter < totalQuestions) {
            currQuestion = list[questionCounter]
            question.text = currQuestion.getQuestion()
            rb1.text = currQuestion.getOpt1()
            rb2.text = currQuestion.getOpt2()
            rb3.text = currQuestion.getOpt3()

            questionCounter++
            NoOfquestions.text = "Questions : $questionCounter / $totalQuestions"

            answered = false
            confirm_next.text = getString(R.string.confirm_answer)

            timeLeftInMillis = COUNTDOWN_IN_MILLIS
            startTimer()
        } else
            finishQuiz()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                /*
                save millisec left in timeLeftMillis to access it outside of this method
                 */
                timeLeftInMillis = millisUntilFinished
                updateCoundownText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateCoundownText()
                checkAnswer()
            }
        }.start()
    }

    private fun updateCoundownText() {
        /*
        * show minutes If you want to show bigger value e.g 1 min or more
        * */
        val minutes: Int = ((timeLeftInMillis / 1000) / 60).toInt()
        val seconds = ((timeLeftInMillis / 1000) % 60).toInt()

        val timeFormat: String = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        timer.text = timeFormat

        if (timeLeftInMillis <= 10000) {
            timer.setTextColor(Color.RED)
        } else
            timer.setTextColor(textDefaultColorcd)
    }

    private fun finishQuiz() {
        val intent = Intent()
        intent.putExtra(EXTRA_SCORE, score)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz()
        } else
            Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT).show()

        backPressedTime = System.currentTimeMillis()
    }

    /*
    * Cancel the timer after the activity is finish to prevent
    * it from running in the background
    * */
    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer == null)
            countDownTimer?.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCORE, score)
        outState.putInt(KEY_QUES_COUNT, questionCounter)
        outState.putBoolean(KEY_ANSWERED, answered)
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis)
        outState.putParcelableArrayList(KEY_QUES_LIST, list)
    }

}