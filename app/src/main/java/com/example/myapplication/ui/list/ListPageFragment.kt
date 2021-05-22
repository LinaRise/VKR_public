package com.example.myapplication.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Sett
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.setCreate.SetUpDialog
import com.example.myapplication.ui.setView.SetViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


class ListPageFragment : Fragment(), ListPageContract.View, SetAdapter.OnSetListener {

    private lateinit var presenter: ListPageContract.Presenter
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var addSetButton: FloatingActionButton
    private var setsDisplayed: LinkedHashMap<Sett, List<String>> = LinkedHashMap()
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var deletedSett: Sett
    private lateinit var deletedSettInfo: List<String>

    lateinit var dbhelper: DBHelper
    private lateinit var adapter: SetAdapter

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
        addSetButton = root.findViewById(R.id.fab)
        dbhelper = DBHelper(requireContext())

//        presenter = ListPagePresenter(this, dbhelper)
        setPresenter(ListPagePresenter(this, DependencyInjectorImpl(dbhelper)))

        addSetButton.setOnClickListener { presenter.onCreateSettTapped() }

        return root
    }

    override fun onStart() {
        super.onStart()
        setsDisplayed.clear()
        presenter.onViewCreated()
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter = SetAdapter(this.context, this, setsDisplayed)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = DefaultItemAnimator()
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.title = getString(R.string.sets)
    }


    override fun setData(setsInfo: LinkedHashMap<Sett, List<String>>) {
        recyclerView.visibility = View.VISIBLE;
        emptyTextView.visibility = View.GONE;
        setsDisplayed = setsInfo
    }

    override fun openDialog() {
        val setUpDialog = SetUpDialog()
        setUpDialog.show(parentFragmentManager, getString(R.string.set_up_dialog))
    }

    override fun showMessage() {
        recyclerView.visibility = View.GONE;
        emptyTextView.visibility = View.VISIBLE;
    }

    override fun onSetClicked(position: Int) {
        val listKeys: List<Sett> = ArrayList<Sett>(setsDisplayed.keys)
        val intent = Intent(requireContext(), SetViewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.putExtra("settId", listKeys[position].settId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        dbhelper.close()

    }

    private var simpleCallBack =
        object : ItemTouchHelper.SimpleCallback(
            0.or(0),
            ItemTouchHelper.LEFT.or(0)
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
                        presenter.onLeftSwipe(position)
                    }
                }
            }

        }

    override fun updateRecyclerViewDeleted(position: Int) {
        adapter.notifyItemRemoved(position)
        setsDisplayed.remove(deletedSett)

    }

    override fun showUndoDeleteWord(position: Int) {
        Snackbar.make(recyclerView, "${deletedSett.settTitle} is deleted", Snackbar.LENGTH_LONG)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    when (event) {
                        DISMISS_EVENT_TIMEOUT ->
                            presenter.onSettDelete(deletedSett)
                    }
                }

            }).setAction(
                "UNDO"
            ) {
                setsDisplayed[deletedSett] = deletedSettInfo
                adapter.notifyItemInserted(position)
            }.show()

    }

    override fun setPresenter(presenter: ListPageContract.Presenter) {
        this.presenter = presenter
    }


}