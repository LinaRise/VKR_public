package com.example.myapplication.ui.chathead

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.setView.SetViewActivity


class CopiedTextAddDialog(
    var setsList: List<Sett>?,
    var word: Word?,
    var openedSet: Sett?,
    dbhelper: DBHelper
) : AppCompatDialogFragment(),
    ConnectivityProvider.ConnectivityStateListener, AdapterView.OnItemSelectedListener {
    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }

    private var hasInternet: Boolean = false
    var mWordRepo: WordRepo = WordRepo(dbhelper)

    //    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.set_pick_up, null)

        val spinner = view.findViewById<Spinner>(R.id.sets_titles_spinner)

        val setsFiltered: List<Sett>? = setsList?.filter { it.settId != openedSet?.settId }
        val setsTitlesMapCopyTo: LinkedHashMap<Long, String> =  LinkedHashMap()

        for (i in setsFiltered?.indices!!){
            setsTitlesMapCopyTo[setsFiltered[i].settId] = setsFiltered[i].settTitle
        }



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
                DialogInterface.OnClickListener { _, _ -> dismiss()})
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                 val pickedSetId =
                    ArrayList<Long>(setsTitlesMapCopyTo.keys)[spinner.selectedItemPosition]

                val intent = Intent(requireContext(), SetViewActivity::class.java)
                intent.putExtra("copiedText", word?.originalWord)
                intent.putExtra("settId", pickedSetId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent)
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


