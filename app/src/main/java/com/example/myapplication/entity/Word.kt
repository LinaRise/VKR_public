package com.example.myapplication.entity

import android.os.Parcel
import android.os.Parcelable

data class Word (

    var wordId: Long=0,
    var originalWord:String="",
    var translatedWord:String="",
    var recallPoint:Long=0,
    var settId:Long = -1,

    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(wordId)
        parcel.writeString(originalWord)
        parcel.writeString(translatedWord)
        parcel.writeLong(recallPoint)
        parcel.writeLong(settId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }

}

