package com.example.myapplication.database.repo.language

import android.os.AsyncTask
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Language
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setView.SetViewAdapter

class LanguageGetAsyncTask(dbhelper: DBHelper) : AsyncTask<Long, Unit, Language>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: Language?) {
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg p0: Long?): Language {
        TODO("Not yet implemented")
    }
}