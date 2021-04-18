package com.example.myapplication.ui.cameraPreview

import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.cameraPreview.camera.ICameraFragmentView

class CameraFragmentPresenter(var cameraFragment: ICameraFragmentView, var dbhelper: DBHelper) {
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)


    fun getAllSetsTitles(): List<Sett>? {
        return mSettRepo.getAll()
    }
}
