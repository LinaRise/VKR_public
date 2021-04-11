package com.example.myapplication.entity

import android.os.Parcel
import android.os.Parcelable

data class Word(

    var wordId: Long = -1,
    var originalWord: String = "",
    var translatedWord: String = "",
    var recallPoint: Int = 0,
    var settId: Long = -1,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(wordId)
        parcel.writeString(originalWord)
        parcel.writeString(translatedWord)
        parcel.writeInt(recallPoint)
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

