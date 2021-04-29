package com.example.myapplication.ui.profile

import android.Manifest
import android.app.*
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.notification.ReminderBroadcast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import java.util.*


class NotificationsFragment : Fragment(),
    ConnectivityProvider.ConnectivityStateListener, SeekBar.OnSeekBarChangeListener,
    OnChartValueSelectedListener,
    ActivityCompat.OnRequestPermissionsResultCallback{
    private val PERMISSION_STORAGE = 0
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

    private var chart: BarChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        /*val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

*/


        tvX = root.findViewById(R.id.tvXMax)
        tvY = root.findViewById(R.id.tvYMax)

        seekBarX = root.findViewById(R.id.seekBar1)
        seekBarX!!.setOnSeekBarChangeListener(this)

        seekBarY = root.findViewById(R.id.seekBar2)
        seekBarY!!.setOnSeekBarChangeListener(this)

        chart = root.findViewById(R.id.chart1)

        chart!!.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart!!.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        chart!!.setPinchZoom(false)

        chart!!.setDrawBarShadow(false)
        chart!!.setDrawGridBackground(false)

        val xAxis = chart!!.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        chart!!.axisLeft.setDrawGridLines(false)

        // setting data

        // setting data
        seekBarX!!.progress = 10
        seekBarY!!.progress = 100

        // add a nice and smooth animation

        // add a nice and smooth animation
        chart!!.animateY(1500)

        chart!!.legend.isEnabled = false


        setHasOptionsMenu(true)

        return root
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvX!!.text = seekBarX!!.progress.toString()
        tvY!!.text = seekBarY!!.progress.toString()
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until seekBarX!!.progress) {
            val multi = (seekBarY!!.progress + 1).toFloat()
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        val set1: BarDataSet
        if (chart!!.data != null &&
            chart!!.data.dataSetCount > 0
        ) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Data Set")
            set1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            set1.setDrawValues(false)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            val data = BarData(dataSets)
            chart!!.data = data
            chart!!.setFitBars(true)
        }
        chart!!.invalidate()
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

            R.id.actionTogglePinch -> {
                if (chart?.isPinchZoomEnabled == true)
                    chart?.setPinchZoom(false);
                else
                    chart?.setPinchZoom(true);

                chart?.invalidate();
                true
            }


            R.id.animateX -> {
                chart?.animateX(2000);
                true;
            }
            R.id.animateY -> {
                chart?.animateY(2000);
                true;
            }
            R.id.animateXY -> {

                chart?.animateXY(2000, 2000);
                true;
            }

            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery(chart!!, "AnotherBarActivity")

                } else {
                    requestStoragePermission(chart)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun requestStoragePermission(view: View?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Snackbar.make(
                view!!.findViewById(R.id.relative_layout_chart)!!,
                "Write permission is required to save image to gallery",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok, object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_STORAGE
                        )
                    }
                }).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Permission Required!",
                Toast.LENGTH_SHORT
            )
                .show()
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToGallery(chart!!, "AnotherBarActivity")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Saving FAILED!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val entry = e as BarEntry

        if (entry.yVals != null) Log.i(
            "VAL SELECTED",
            "Value: " + entry.yVals[h!!.stackIndex]
        ) else Log.i("VAL SELECTED", "Value: " + entry.y)
    }


    private fun saveToGallery(chart: BarChart, name: String) {
        if (chart!!.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) Toast.makeText(
            requireContext(), "Saving SUCCESSFUL!",
            Toast.LENGTH_SHORT
        ).show() else Toast.makeText(
            requireContext(),
            "Saving FAILED!",
            Toast.LENGTH_SHORT
        )
            .show()
    }



    override fun onNothingSelected() {}

    private fun getColors(): ArrayList<Int> {

        // have as many colors as stack-values per entry
        val colors = ArrayList<Int>(3)
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3)
        return colors
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