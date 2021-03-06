package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.database.DBHelper
import com.example.myapplication.ui.DependencyInjectorImpl
import com.example.myapplication.ui.chathead.TranslateBubbleService
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), MainContract.View {
    lateinit var dbhelper: DBHelper
    private var switchAB: ToggleButton? = null
    private lateinit var presenter:MainContract.Presenter
    override fun showDrawOverAppPermission() {
      Toast.makeText(this,getString(R.string.draw_permission),Toast.LENGTH_SHORT).show()
    }

    override fun showTranslateBubbleOn() {
        Toast.makeText(this,getString(R.string.translate_bubble_on),Toast.LENGTH_SHORT).show()

    }

    override fun showTranslateBubbleOff() {
        Toast.makeText(this,getString(R.string.translate_bubble_off),Toast.LENGTH_SHORT).show()

    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbhelper = DBHelper(this)
        setPresenter(MainPresenter(this, DependencyInjectorImpl(dbhelper)))

        //находим файл xml с BottomNavigationView
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // находим файл xml с фрагментом к которму будет прикреплять BottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment)
        // Передаем ID каждого меню как set ID
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_list, R.id.navigation_camera, R.id.navigation_profile
            )
        )
        //Настривает ActionBar возвращенный  [AppCompatActivity.getSupportActionBar] для использования
        //с [NavController].
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
        }
    }



    /**
     * Set and initialize the view elements.
     */


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_switch_layout, menu)
        val item: MenuItem = menu.findItem(R.id.switchId) as MenuItem
        item.setActionView(R.layout.switch_layout)
        switchAB = item.actionView.findViewById(R.id.toggle_btn)
        switchAB?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showTranslateBubbleOn()
                startService(Intent(this@MainActivity, TranslateBubbleService::class.java))
            } else {
                val myService = Intent(this@MainActivity, TranslateBubbleService::class.java)
                stopService(myService)
                showTranslateBubbleOff()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(this)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            ) {
//                initializeView()
            } else { //Permission is not available
               showDrawOverAppPermission()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084
    }

}