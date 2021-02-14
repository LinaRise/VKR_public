package com.example.myapplication.database.repo.sett

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Sett
import java.nio.file.Files.delete

class SetDeleteAsyncTask(dbhelper: DBHelper) : AsyncTask<Sett, Unit, Unit>() {

    var mSettRepo: SettRepo = SettRepo(dbhelper)

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
//        Log.d("DEBUG_TAG", "Language inserted with id $result")

    }

    override fun doInBackground(vararg p0: Sett?) {
            val sett = p0[0] as Sett
            val wordId = mSettRepo.delete(sett)
//        mSetWordRepo.delete(SetWord(settId = settId, wordId = wordId))

    }
}
