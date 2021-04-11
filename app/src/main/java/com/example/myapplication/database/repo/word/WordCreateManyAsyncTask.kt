package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import android.util.Log
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word

//import com.example.myapplication.entity.Word

class WordCreateManyAsyncTask(var dbhelper: DBHelper) : AsyncTask<List<Word>, Unit, Unit>() {
    private val TAG = "WordCreateManyAsyncTask";
    var mWordRepo: WordRepo = WordRepo(dbhelper)
//    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d(TAG, "onPreExecute")
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        Log.d(TAG, "onPostExecute")

//        Log.d("DEBUG_TAG", "Language inserted with id $result")

    }


    override fun doInBackground(vararg p0: List<Word>) {
        val wordList = p0[0] as ArrayList<Word>
        for (word in wordList) {
//        val wordId = mWordRepo.create(word)

            val wordId = mWordRepo.create(word)
            Log.d(TAG, wordId.toString())

        }
        Log.d(TAG, "doInBackground")

    }


}