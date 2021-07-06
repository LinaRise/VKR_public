package com.example.myapplication.ui.setView.copyCardDialog

import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface CopyCardContract {
    interface Presenter : BasePresenter {
        fun onAddWordClicked(word: Word?, entity: Sett)
    }

    interface View : BaseView<Presenter> {
        fun showCopiedWordToSetToast()
    }
}