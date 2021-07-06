package com.example.myapplication.ui.profile

import com.example.myapplication.entity.StudyProgress
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.github.mikephil.charting.data.BarEntry

interface ProfileContract {
    interface Presenter : BasePresenter {
       fun onViewCreated()
    }

    interface View : BaseView<Presenter> {
        fun setData(
            list: List<StudyProgress>,
            values: List<BarEntry>,
            labels: List<String>
        )

    }
}
