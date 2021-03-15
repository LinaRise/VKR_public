package com.example.myapplication.database.repo.word

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word

class WordUpdateManyAsyncTask(dbhelper: DBHelper) : AsyncTask<List<Word>, Unit, Unit>() {

    var mWordRepo: WordRepo = WordRepo(dbhelper)
//    var mSetWordRepo: SetWordRepo = SetWordRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
//        Log.d("DEBUG_TAG", "Language inserted with id $result")

    }


    override fun doInBackground(vararg p0: List<Word>) {
        val wordList = p0[0] as ArrayList
        for (word in wordList) {
            mWordRepo.update(word)
        }
    }
}
