package com.example.android.myquizapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.android.myquizapp.QuizContract.QuizTable

class QuizDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME: String = "myquiz.db"
        private const val DATABASE_VERSION: Int = 1
    }

    lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        this.db = db!!
        val quizTable: String = "CREATE TABLE ${QuizTable.TABLE_NAME} " +
                "(${QuizTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${QuizTable.COL_QUESTION} TEXT," +
                "${QuizTable.OPTION1} TEXT," +
                "${QuizTable.OPTION2} TEXT," +
                "${QuizTable.OPTION3} TEXT," +
                "${QuizTable.ANSWER_NO} INTEGER)"
        db.execSQL(quizTable)
        fillTable()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${QuizTable.TABLE_NAME}")
        onCreate(db)
    }

    private fun fillTable() {
        val question1 = Question("Who is the Founder of SpaceX?", "Jeff Bezos", "Elon Musk", "Richard Branson", 2)
        addQuestions(question1)
        val question2 = Question("Name of the spacecraft recently launched by spaceX and NASA.", "Falcon 1", "Falcon Heavy", "SpaceX crew Dragon", 3)
        addQuestions(question2)
        val question3 = Question("Who is the Chairman of Tata Group?", "Ratan Tata", "Cyrus Mistry", "Naval Tata", 1)
        addQuestions(question3)
        val question4 = Question("2 + 2", "1", "5", "4", 3)
        addQuestions(question4)
        val question5 = Question("15 + 5", "30", "10", "20", 3)
        addQuestions(question5)
        val question6 = Question("25 + 42", "67", "65", "71", 1)
        addQuestions(question6)
        val question7 = Question("What is the formula for Methane", "CPH4", "CH4", "C2H6", 2)
        addQuestions(question7)
        val question8 = Question("Which company acquired Instagram?", "Facebook", "Google", "Amazon", 1)
        addQuestions(question8)
        val question9 = Question("Which gas is safe and an effective extinguisher for all confined fires?", "Nitrogen dioxide", "Carbon dioxide", "Sulphur dioxide", 2)
        addQuestions(question9)
        val question10 = Question("Which is the tallest building on the Earth?", "World Trade Center", "Shanghai Tower", "Burj Khalifa", 3)
        addQuestions(question10)
    }

    private fun addQuestions(question: Question) {
        val cv = ContentValues()
        cv.put(QuizTable.COL_QUESTION, question.getQuestion())
        cv.put(QuizTable.OPTION1, question.getOpt1())
        cv.put(QuizTable.OPTION2, question.getOpt2())
        cv.put(QuizTable.OPTION3, question.getOpt3())
        cv.put(QuizTable.ANSWER_NO, question.getAnswerNo())
        db.insert(QuizTable.TABLE_NAME, null, cv)
    }

    fun getAllQuestions(): java.util.ArrayList<Question> {
        val list: java.util.ArrayList<Question> = ArrayList()
        db = readableDatabase
        val c: Cursor = db.rawQuery("SELECT * FROM ${QuizTable.TABLE_NAME}", null)

        if (c.moveToFirst()) {
            do {
                val ques = Question()
                ques.setQuestion(c.getString(c.getColumnIndex(QuizTable.COL_QUESTION)))
                ques.setOpt1(c.getString(c.getColumnIndex(QuizTable.OPTION1)))
                ques.setOpt2(c.getString(c.getColumnIndex(QuizTable.OPTION2)))
                ques.setOpt3(c.getString(c.getColumnIndex(QuizTable.OPTION3)))
                ques.setAnswerNo(c.getInt(c.getColumnIndex(QuizTable.ANSWER_NO)))
                list.add(ques)
            } while (c.moveToNext())
        }
        c.close()
        return list
    }
}