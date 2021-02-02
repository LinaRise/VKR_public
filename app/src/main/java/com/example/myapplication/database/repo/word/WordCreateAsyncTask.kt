package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.SetWordRepo
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.SetWord
import com.example.myapplication.entity.Word
import java.util.*
import kotlin.collections.ArrayList

class WordCreateAsyncTask(dbhelper: DBHelper) : AsyncTask<Any, Unit,Unit>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
//        Log.d("DEBUG_TAG", "Language inserted with id $result")

    }


    override fun doInBackground(vararg p0: Any) {
        val wordList = p0[0] as ArrayList<Word>
        val settId = p0[1] as Long
        for (word in wordList) {
            val wordId = mWordRepo.create(word)
            mSetWordRepo.create(SetWord(settId = settId, wordId = wordId))
        }
    }


}