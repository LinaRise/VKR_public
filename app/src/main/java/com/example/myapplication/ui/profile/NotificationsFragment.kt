package com.example.myapplication.ui.profile

import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.notification.ReminderBroadcast
import java.util.*
import kotlin.math.min


class NotificationsFragment : Fragment(),
    ConnectivityProvider.ConnectivityStateListener {

    private val REQUEST_CODE_NOTIFICATION: Int = 0
    private val CHANNEL_ID = "STUDY_NOTIFY_CHANNEL"
    private val mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0

    private var dateAndTime = Calendar.getInstance()

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_switch_backup_layout, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.study_reminder_menu_item -> {
                callTimePicker()


                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callTimePicker() {
        // получаем текущее время
        val cal = Calendar.getInstance()
        mHour = cal[Calendar.HOUR_OF_DAY]
        mMinute = cal[Calendar.MINUTE]

        // инициализируем диалог выбора времени текущими значениями
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
            OnTimeSetListener { _, hourOfDay, minute ->
                createNotificationChannel()
                val intent = Intent(activity, ReminderBroadcast::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    REQUEST_CODE_NOTIFICATION,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                val alarmManager =
                    activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                Toast.makeText(requireContext(), "$hourOfDay : $minute", Toast.LENGTH_SHORT).show()
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = resources.getString(R.string.channel_name)
            val descriptionText = resources.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                ContextCompat.getSystemService(
                    requireContext(),
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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