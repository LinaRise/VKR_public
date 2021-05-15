package com.example.myapplication.ui.setView

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word


class CopyCardDialog(
    var setsList: List<Sett>,
    var word: Word?,
    var openedSet: Sett?,
    var dbhelper: DBHelper
) : AppCompatDialogFragment(),
    ConnectivityProvider.ConnectivityStateListener, AdapterView.OnItemSelectedListener {
    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }

    private var hasInternet: Boolean = false
    var mWordRepo: WordRepo = WordRepo(dbhelper)
    var mSettRepo: SettRepo = SettRepo(dbhelper)

    //    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.set_pick_up, null)

        var spinner = view.findViewById<Spinner>(R.id.sets_titles_spinner)
        val setsTitlesMapCopyTo: LinkedHashMap<Long, String> =
            setsList.map { it.settId to it.settTitle }.toMap()
                .filter { it.key != openedSet?.settId } as LinkedHashMap<Long, String>





        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            setsTitlesMapCopyTo.values.toTypedArray()
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        alertDialogBuilder.setView(view)
            .setTitle("Pick up Set")
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                val pickedSetId =
                    ArrayList<Long>(setsTitlesMapCopyTo.keys)[spinner.selectedItemPosition]
                word!!.settId = pickedSetId
                mWordRepo.create(word!!)
                var pickedSet = setsList!!.filter { it.settId == pickedSetId }
                pickedSet[0].wordsAmount = pickedSet[0].wordsAmount + 1
                mSettRepo.update(pickedSet[0])
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            })




        return alertDialogBuilder.create()


    }


    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}


