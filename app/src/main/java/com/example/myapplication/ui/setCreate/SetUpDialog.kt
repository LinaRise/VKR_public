package com.example.myapplication.ui.setCreate

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.*
import android.text.Editable
import android.text.TextWatcher
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


class SetUpDialog : AppCompatDialogFragment(), ConnectivityProvider.ConnectivityStateListener {
    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }


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

    private var editTextTitle: EditText? = null
    private var editTextInputLang: AutoCompleteTextView? = null
    private var editTextOutputLang: AutoCompleteTextView? = null

    var languageTitleAndCode: Map<String, String> = hashMapOf()


    //    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.set_parameters, null)

        editTextTitle = view.findViewById(R.id.edit_set_title)
        editTextInputLang = view.findViewById(R.id.edit_language_input) as AutoCompleteTextView
        editTextOutputLang = view.findViewById(R.id.edit_language_output) as AutoCompleteTextView


        var checkBox = view.findViewById<CheckBox>(R.id.checkbox)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                hasAutoSuggest = if (isChecked) {
//                    Toast.makeText(requireContext(), "Checked", Toast.LENGTH_SHORT).show()
                    1
                } else {
//                    Toast.makeText(requireContext(), "Unchecked", Toast.LENGTH_SHORT).show()
                    0
                }
            }

        editTextTitle!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val dialog = dialog as AlertDialog?
                dialog!!.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
            }
        })

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
                intent.putExtra("hasAutoSuggest", hasAutoSuggest)
                startActivity(intent)

                /*  editTextTitle?.hint = "Enter title"

                    val v =
                        requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v!!.vibrate(
                            VibrationEffect.createOneShot(
                                50,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        //deprecated in API 26
                        v!!.vibrate(50)
                    }
                    break
                }*/
            })




        return alertDialogBuilder.create()


    }
    override fun onResume() {
        super.onResume()

        // disable positive button by default
        val dialog = dialog as AlertDialog?
        dialog!!.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }



    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }


    private fun loadAvailableLanguagesForTranslation() {
        if (hasInternet) {
            translateService
            var languagesSourceNames: Array<String> = arrayOf()
            var languages: List<Language> = listOf()
            if (translate != null) {
                languages = translate!!.listSupportedLanguages()
                languagesSourceNames = languages.map { it.name }.toTypedArray()
                languageTitleAndCode = languages.map { it.name to it.code }.toMap()
            }

            val adapterSource = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                languagesSourceNames

            )
            val languagesTarget =
                translate!!.listSupportedLanguages(
                    Translate.LanguageListOption.targetLanguage(
                        languageTitleAndCode[editTextInputLang!!.text.toString()]
                    )
                )

            val languagesTargetNames = languagesTarget.map { it.name }.toTypedArray()
            val adapterTarget = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                languagesTargetNames

            )
            editTextInputLang!!.setAdapter(adapterSource)
            editTextOutputLang!!.setAdapter(adapterTarget)
        } else {
            Toast.makeText(requireContext(), "No internet connection!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()

        loadAvailableLanguagesForTranslation()
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }
}


