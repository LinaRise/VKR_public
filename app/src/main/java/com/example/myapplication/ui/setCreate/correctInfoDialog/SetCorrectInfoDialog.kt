package com.example.myapplication.ui.setCreate.correctInfoDialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.database.DBHelper
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.setCreate.ISetInputData
import com.example.myapplication.ui.setCreate.setUpDialog.SetUpContract
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import java.io.IOException
import java.util.concurrent.Executors

class SetCorrectInfoDialog : AppCompatDialogFragment(), SetCorrectInfoContract.View,
    ConnectivityProvider.ConnectivityStateListener {

    private lateinit var presenter: SetCorrectInfoContract.Presenter

    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }
    var languageTitleAndCode: Map<String, String> = hashMapOf()
    lateinit var dbhelper: DBHelper


    private var hasInternet: Boolean = false

    companion object {

        private var hasAutoSuggest = 0;
    }

    /**
     * инициализия сервиса перевода для загрузки доступных языков
     */
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


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val view = inflater.inflate(R.layout.set_parameters, null)
        dbhelper = DBHelper(requireContext())
        setPresenter(SetCorrectInfoPresenter(this, DependencyInjectorImpl(dbhelper)))

        val setTitleReceived = requireArguments().getString("settTitle")
        editTextTitle = view.findViewById(R.id.edit_set_title)
        editTextInputLang = view.findViewById(R.id.edit_language_input) as AutoCompleteTextView
        editTextOutputLang = view.findViewById(R.id.edit_language_output) as AutoCompleteTextView

        val checkBox = view.findViewById<CheckBox>(R.id.checkbox) as CheckBox
//        Toast.makeText(requireContext(), setTitleReceived, Toast.LENGTH_SHORT).show()
        val inputLangReceived =   requireArguments().getString("inputLanguage")
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

        editTextTitle!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //включаем кнопку "Ок", если названия сета не пустое
                val dialog = dialog as AlertDialog?
                dialog!!.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    editTextTitle!!.text.trim().isNotEmpty()

            }
        })

        alertDialogBuilder.setView(view)
            .setTitle(R.string.edit_set)
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { _, _ ->
                val setTitle: String = editTextTitle?.text.toString()
                val inputLang: String = editTextInputLang?.text.toString()
                val outputLang: String = editTextOutputLang?.text.toString()
                val list = ArrayList<Any>()
                list.add(setTitle)
                list.add(inputLang)
                list.add(outputLang)
                list.add(hasAutoSuggest)
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

        //загрузка доступных языков
        if (hasInternet) {
            if (languageTitleAndCode.values.isEmpty()) {
                translateService
                presenter.onViewCreated(translate)

            }
        }


    }

    override fun showNoInternetConnectionToast() {
        Toast.makeText(
            requireContext(),
            "No internet connection!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun setAvailableLanguagesInfo(languageTitleAndCode: Map<String, String>) {
        this.languageTitleAndCode = languageTitleAndCode

        if (languageTitleAndCode.values.isNotEmpty()) {

            val adapterSource = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                languageTitleAndCode.values.toTypedArray()

            )
            val adapterTarget = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                languageTitleAndCode.values.toTypedArray()
            )
            editTextInputLang!!.setAdapter(adapterSource)
            editTextOutputLang!!.setAdapter(adapterTarget)

        } else {
            showNoInternetConnectionToast()
        }
    }


    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
        if (!hasInternet)
            showNoInternetConnectionToast()
    }

    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

    override fun setPresenter(presenter: SetCorrectInfoContract.Presenter) {
        this.presenter = presenter
    }
}