package com.example.myapplication.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.setCreate.SetCreateActivity
import com.example.myapplication.ui.setCreate.SetUpDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListPageFragment : Fragment(),IListPageView {
    private lateinit var presenter: ListPagePresenter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var addSetButton: FloatingActionButton
    private var setsDisplayed: ArrayList<Sett> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.set_list)

        presenter = ListPagePresenter(this)


        getSetsList()
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = SetAdapter(this.context, setsDisplayed)
        recyclerView.adapter = adapter
//       val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        addSetButton = root.findViewById(R.id.fab)
        addSetButton.setOnClickListener { clickOnAddSetButton() }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = "Sets"
    }

    private fun clickOnAddSetButton() {
        presenter.openSet()
    }

    private fun getSetsList() {
        presenter.loadData()
    }

    override fun setData(sets: List<Sett>) {
       setsDisplayed.addAll(sets)
    }

    override fun openDialogForSetCreation() {
        val setUpDialog = SetUpDialog()
        setUpDialog.show(parentFragmentManager,"Set Up Dialog")
//        val intent = Intent(activity, SetCreateActivity::class.java)
//        startActivity(intent)
    }


}