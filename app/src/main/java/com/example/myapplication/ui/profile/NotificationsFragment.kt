package com.example.myapplication.ui.profile

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider


class NotificationsFragment : Fragment(),
    ConnectivityProvider.ConnectivityStateListener {

    private var hasInternet: Boolean = false
    private val TAG = "NotificationsFragment"

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val provider: ConnectivityProvider by lazy {
        ConnectivityProvider.createProvider(
            requireContext()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        setHasOptionsMenu(true)

        return CanadaChart(requireContext())
    }

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)

    }






    private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
        return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
    }

    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        hasInternet = state.hasInternet()
        if (!hasInternet) {
            Toast.makeText(requireContext(), "Internet is not available", Toast.LENGTH_LONG).show()
        }
    }


}