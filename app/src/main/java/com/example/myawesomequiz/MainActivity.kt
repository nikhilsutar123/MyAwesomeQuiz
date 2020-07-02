package com.example.myawesomequiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.android.myquizapp.QuizSCreen

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CODE = 1
    private var highscore = 0
    private val SHARED_PREFS = "sharedPrefs"
    private val HIGHSCORE_KEY = "highscoreKey"

    private lateinit var highscore_tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        highscore_tv = findViewById(R.id.highscore)
        loadHighscore()

        val startQuiz: Button = findViewById(R.id.start_button)
        startQuiz.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        /*
        When button is pressed user goes to the Quiz Screen
         */
        val intent = Intent(this, QuizSCreen::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val score = data!!.getIntExtra(QuizSCreen.EXTRA_SCORE, 0)
                if (score > highscore) {
                    updateHighscore(score)
                }
            }
        }
    }
    /*
    Retrives the current Highscore stored in shard preferences file to display
    it in the MainActivity
     */
    private fun loadHighscore() {
        val prefs: SharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        highscore = prefs.getInt(HIGHSCORE_KEY,0)
        highscore_tv.text = "Highscore: $highscore"
    }

    /*
    gets the latest score to update the Highscore value
    */
    private fun updateHighscore(score: Int) {
        highscore = score
        highscore_tv.text = "Highscore: $highscore"

        val prefs: SharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        prefsEditor.putInt(HIGHSCORE_KEY, highscore)
        prefsEditor.apply()
    }
}