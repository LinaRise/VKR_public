package com.example.myapplication.ui.setCreate

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R

class SetCorrectInfoDialog : AppCompatDialogFragment() {
    private var mCallback: ISetInputData? = null


    private var editTextTitle: EditText? = null
    private var editTextInputLang: AutoCompleteTextView? = null
    private var editTextOutputLang: AutoCompleteTextView? = null
    private var hasAutoSuggest = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.set_parameters, null)
        val setTitleReceived = requireArguments().getString("settTitle")
        editTextTitle = view.findViewById(R.id.edit_set_title)
        editTextInputLang = view.findViewById(R.id.edit_language_input) as AutoCompleteTextView
        editTextOutputLang = view.findViewById(R.id.edit_language_output) as AutoCompleteTextView
        val checkBox = view.findViewById<CheckBox>(R.id.checkbox) as CheckBox
        Toast.makeText(requireContext(), setTitleReceived, Toast.LENGTH_SHORT).show()
        val inputLangReceived = requireArguments().getString("inputLanguage")
        val outputLangReceived = requireArguments().getString("outputLanguage")
        val hasAutoSuggestReceived = requireArguments().getInt("hasAutoSuggest")
        hasAutoSuggest = hasAutoSuggestReceived
        checkBox.isChecked = hasAutoSuggestReceived == 1



        editTextTitle?.setText(setTitleReceived)
        editTextInputLang?.setText(inputLangReceived, TextView.BufferType.EDITABLE)
        editTextOutputLang?.setText(outputLangReceived, TextView.BufferType.EDITABLE)

        checkBox
            .setOnCheckedChangeListener { buttonView, isChecked ->
                hasAutoSuggest = if (isChecked) {
                    Toast.makeText(requireContext(), "Checked", Toast.LENGTH_SHORT).show()
                    1
                } else {
                    Toast.makeText(requireContext(), "Unchecked", Toast.LENGTH_SHORT).show()
                    0
                }
            }

        alertDialogBuilder.setView(view)
            .setTitle("Edit Set")
            .setNegativeButton("cancel",
                DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                val setTitle: String = editTextTitle?.text.toString()
                val inputLang: String = editTextInputLang?.text.toString()
                val outputLang: String = editTextOutputLang?.text.toString()
                val list = ArrayList<Any>()
                list.add(setTitle)
                list.add(inputLang)
                list.add(outputLang)
                list.add(hasAutoSuggest)
                Toast.makeText(requireContext(),hasAutoSuggest.toString(),Toast.LENGTH_SHORT).show()
//                listener.applyTexts(setTitle, inputLang,outputLang);
                /* targetFragment!!.onActivityResult(
                     targetRequestCode,
                     Activity.RESULT_OK,
                     requireActivity().intent
                 )*/
                mCallback?.onInputedData(list)
            })

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.available_translation_languages,
            android.R.layout.simple_list_item_1
        )
        editTextInputLang!!.setAdapter(adapter)
        editTextOutputLang!!.setAdapter(adapter)
        return alertDialogBuilder.create()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val a: Activity?
        if (context is Activity) {
            a = context
            try {
                mCallback = activity as ISetInputData
            } catch (e: ClassNotFoundException) {
                Log.d(
                    "SetCorrectInfoDialog",
                    "Activity doesn't implement the ISelectedData interface"
                );
            }
        }
    }
}