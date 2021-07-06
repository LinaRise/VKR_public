package com.example.myapplication.ui.list

import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface ListPageContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onCreateSettTapped()
        fun onLeftSwipe(position: Int)
        fun onSettDelete(sett: Sett)
    }

    interface View : BaseView<Presenter> {
        fun setData(setsInfo: LinkedHashMap <Sett, List<String>>)
        fun openDialog()
        fun showMessage()
        fun updateRecyclerViewDeleted(position: Int)
        fun showUndoDeleteWord(position: Int)
    }
}
