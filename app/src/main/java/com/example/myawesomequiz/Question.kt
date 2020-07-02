package com.example.android.myquizapp

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Question() : Parcelable {
    private var question: String? = ""
    private var option1: String? = ""
    private var option2: String? = ""
    private var option3: String? = ""
    private var answerNr = 0

    constructor(parcel: Parcel) : this() {
        question = parcel.readString()
        option1 = parcel.readString()
        option2 = parcel.readString()
        option3 = parcel.readString()
        answerNr = parcel.readInt()
    }

    constructor(question: String, option1: String, option2: String, option3: String, answerNr: Int) : this() {
        this.question = question
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.answerNr = answerNr
    }


    fun getQuestion(): String? {
        return question
    }

    fun setQuestion(question: String) {
        this.question = question
    }

    fun getOpt1(): String? {
        return option1
    }

    fun setOpt1(option1: String) {
        this.option1 = option1
    }

    fun getOpt2(): String? {
        return option2
    }

    fun setOpt2(option2: String) {
        this.option2 = option2
    }

    fun getOpt3(): String? {
        return option3
    }

    fun setOpt3(option3: String) {
        this.option3 = option3
    }

    fun getAnswerNo(): Int {
        return answerNr
    }

    fun setAnswerNo(answerNr: Int) {
        this.answerNr = answerNr
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(option1)
        parcel.writeString(option2)
        parcel.writeString(option3)
        parcel.writeInt(answerNr)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Question?> {
        override fun newArray(size: Int): Array<Question?> {
            return newArray(size)
        }

        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

    }

}
