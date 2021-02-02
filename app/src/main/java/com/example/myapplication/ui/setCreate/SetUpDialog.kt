package com.example.myapplication.ui.setCreate

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R


class SetUpDialog : AppCompatDialogFragment() {

    companion object {
        private val LANGAUGES = arrayListOf<String>("Russian", "English", "French", "German","Czech")
    }

    private var editTextTitle: EditText? = null
    private var editTextInputLang: AutoCompleteTextView? = null
    private var editTextOutputLang: AutoCompleteTextView? = null

    //    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.set_parameters, null)
        alertDialogBuilder.setView(view)
            .setTitle("New Set")
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                val setTitle: String = editTextTitle?.text.toString()
                val inputLang: String = editTextInputLang?.text.toString()
                val outputLang: String = editTextOutputLang?.text.toString()
//                listener.applyTexts(setTitle, inputLang,outputLang);
                val intent = Intent(activity, SetCreateActivity::class.java)
                intent.putExtra("setTitle", setTitle)
                intent.putExtra("inputLanguage", inputLang)
                intent.putExtra("outputLanguage", outputLang)
                startActivity(intent)
            })
        editTextTitle = view.findViewById(R.id.edit_set_title)
        editTextInputLang = view.findViewById(R.id.edit_language_input) as AutoCompleteTextView
        editTextOutputLang = view.findViewById(R.id.edit_language_output) as AutoCompleteTextView
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.available_translation_languages, android.R.layout.simple_list_item_1)
        editTextInputLang!!.setAdapter(adapter)
        editTextOutputLang!!.setAdapter(adapter)
        return alertDialogBuilder.create()


    }



    /*  override fun onAttach(context: Context) {
          super.onAttach(context)
          listener = try {
              context as SetUpDialogListener
          } catch (e: ClassCastException) {
              throw ClassCastException(
                  context.toString() +
                          "must implement SetUpDialogListener"
              )
          }
      }*/

/*interface SetUpDialogListener {
    fun applyTexts(setTitle: String?, languageInput: String?, languageOutput: String?)
}*/
}


