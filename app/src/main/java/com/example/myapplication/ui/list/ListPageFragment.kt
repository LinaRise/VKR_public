package com.example.myapplication.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.entity.Set
import com.example.myapplication.ui.setAdd.SetCreateActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListPageFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var addSetButton: FloatingActionButton
    var states: ArrayList<Set> = ArrayList<Set>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.set_list)
        setInitialData();
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val adapter = SetAdapter(this.context, states)
//       val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        addSetButton = root.findViewById(R.id.fab)
        addSetButton.setOnClickListener { clickOnAddSetButton() }
        recyclerView.adapter = adapter
        return root
    }

    private fun clickOnAddSetButton() {
        val intent = Intent(activity, SetCreateActivity::class.java)
        startActivity(intent)
    }

    private fun setInitialData() {
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
        states.add(Set("Бразилия", "Бразилиа"))
    }
}