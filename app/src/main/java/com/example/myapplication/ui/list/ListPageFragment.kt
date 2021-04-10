package com.example.myapplication.ui.list

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SetDeleteAsyncTask
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.setCreate.SetUpDialog
import com.example.myapplication.ui.setView.SetViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


class ListPageFragment : Fragment(), IListPageView, SetAdapter.OnSetListener {
    private lateinit var presenter: ListPagePresenter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var addSetButton: FloatingActionButton
    private var setsDisplayed: LinkedHashMap<Sett, List<String>> = LinkedHashMap()
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var deletedSett: Sett
    private lateinit var deletedSettInfo: List<String>
    lateinit var dbhelper: DBHelper
    lateinit var db: SQLiteDatabase
    private lateinit var adapter: SetAdapter
    private var allowRefresh: Boolean = true
    lateinit var handlerForDeleteSett: Handler
    private lateinit var deleteSettRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        recyclerView = root.findViewById(R.id.set_list)
        emptyTextView = root.findViewById(R.id.empty_view) as TextView
        dbhelper = DBHelper(requireContext())

        presenter = ListPagePresenter(this, dbhelper)

//       val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        addSetButton = root.findViewById(R.id.fab)
        addSetButton.setOnClickListener { clickOnAddSetButton() }

        return root
    }

    override fun onStart() {
        super.onStart()
        setsDisplayed.clear()
        getSetsList()
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = SetAdapter(this.context, this, setsDisplayed)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

//    override fun onResume() {
//        super.onResume()
////        setsDisplayed.clear() //clear array
//         // notify adapter
//        getSetsList() // getdatas from service
//        adapter.notifyDataSetChanged()
//    }


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

    override fun setData(setsInfo: LinkedHashMap<Sett, List<String>>) {
        recyclerView.visibility = View.VISIBLE;
        emptyTextView.visibility = View.GONE;
        setsDisplayed = setsInfo
//        adapter.notifyDataSetChanged()
    }

    override fun openDialogForSetCreation() {
        val setUpDialog = SetUpDialog()
        setUpDialog.show(parentFragmentManager, "Set Up Dialog")
//        val intent = Intent(activity, SetCreateActivity::class.java)
//        startActivity(intent)
    }

    override fun showMessage() {
        recyclerView.visibility = View.GONE;
        emptyTextView.visibility = View.VISIBLE;
    }

    override fun onSetClicked(position: Int) {
        val listKeys: List<Sett> = ArrayList<Sett>(setsDisplayed.keys)
        var intent = Intent(requireContext(), SetViewActivity::class.java)
        intent.putExtra("settId", listKeys[position].settId)
        startActivity(intent)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        dbhelper.close()
//
//    }

    private var simpleCallBack =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deletedSett = ArrayList<Sett>(setsDisplayed.keys)[position]
                        deletedSettInfo = setsDisplayed[deletedSett]!!
                        presenter.deleteSettShow(deletedSett, position)


                    }
                }
            }

        }

    override fun updateRecyclerViewDeleted(position: Int) {
        adapter.notifyItemRemoved(position)
        setsDisplayed.remove(deletedSett)

    }

    override fun showUndoDeleteWord(position: Int) {
        /*Snackbar.make(
            recyclerView,
            "${deletedSett.settTitle} is deleted",
            Snackbar.LENGTH_LONG
        ).setAction(
            "UNDO"
        ) {
            setsDisplayed.put(deletedSett)
            adapter.notifyItemInserted(position)
        }.addCallback(Snackbar.Callback() {

        })
            .show()*/

        Snackbar.make(recyclerView, "${deletedSett.settTitle} is deleted", Snackbar.LENGTH_LONG)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    when (event) {
                        DISMISS_EVENT_TIMEOUT ->
                            presenter.deleteSettFromDb(deletedSett)
                    }
                }

            }).setAction(
                "UNDO"
            ) {
                setsDisplayed.put(deletedSett, deletedSettInfo)
                adapter.notifyItemInserted(position)
            }.show()

    }


}