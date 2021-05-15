package com.example.myapplication.ui.setCreate

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import java.io.IOException
import java.util.concurrent.Executors

class SetCorrectInfoDialog : AppCompatDialogFragment(),
    ConnectivityProvider.ConnectivityStateListener {

    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }
    var languageTitleAndCode: Map<String, String> = hashMapOf()


    private var hasInternet: Boolean = false

    companion object {

        private var hasAutoSuggest = 0;
    }

    var translate: Translate? = null
    private val translateService: Unit
        get() {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {

                resources.openRawResource(R.raw.credentials).use { `is` ->

                    //Get credentials:
                    val myCredentials = GoogleCredentials.fromStream(`is`)

                    //Set credentials and get translate service:
                    val translateOptions =
                        TranslateOptions.newBuilder().setCredentials(myCredentials).build()
                    translate = translateOptions.service
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }

    private var mCallback: ISetInputData? = null


    private var editTextTitle: EditText? = null
    private var editTextInputLang: AutoCompleteTextView? = null
    private var editTextOutputLang: AutoCompleteTextView? = null
    private var hasAutoSuggest = 0

    var languagesSourceNames: Array<String> = arrayOf()


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
            .setOnCheckedChangeListener { _, isChecked ->
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
                Toast.makeText(requireContext(), hasAutoSuggest.toString(), Toast.LENGTH_SHORT)
                    .show()
                mCallback?.onInputedData(list)
            })

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

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            if (hasInternet) {
                if (languagesSourceNames.isEmpty()) {
                    loadAvailableLanguagesForTranslation()
                }
            }
            handler.post {
                if (languagesSourceNames.isNotEmpty()) {
                    /*val languagesTarget =
                        translate!!.listSupportedLanguages(
                            Translate.LanguageListOption.targetLanguage(
                                languageTitleAndCode[editTextInputLang!!.text.toString()]
                            )
                        )*/

//                    val languagesTargetNames = languagesTarget.map { it.name }.toTypedArray()


                    val adapterSource = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        languagesSourceNames

                    )
                    val adapterTarget = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        languagesSourceNames

                    )
                    editTextInputLang!!.setAdapter(adapterSource)
                    editTextOutputLang!!.setAdapter(adapterTarget)

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Can't load available languages for translation! No internet connection!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }


    private fun loadAvailableLanguagesForTranslation() {
        translateService
        var languages: List<Language> = listOf()
        if (translate != null) {
            languages = translate!!.listSupportedLanguages()
            languagesSourceNames = languages.map { it.name }.toTypedArray()
            languageTitleAndCode = languages.map { it.name to it.code }.toMap()
        }

    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
        if (!hasInternet)
            Toast.makeText(
                requireContext(),
                "No internet connection!",
                Toast.LENGTH_SHORT
            ).show()

    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }
}