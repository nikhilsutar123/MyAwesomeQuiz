package com.example.android.myquizapp

import android.provider.BaseColumns

class QuizContract private constructor(){
     class QuizTable : BaseColumns{
         companion object{
             val ID = BaseColumns._ID
             const val TABLE_NAME = "quiz_questions"
             const val COL_QUESTION = "question"
             const val OPTION1 = "option_1"
             const val OPTION2 = "option_2"
             const val OPTION3 = "option_3"
             const val ANSWER_NO = "answer_no"
         }
     }

}