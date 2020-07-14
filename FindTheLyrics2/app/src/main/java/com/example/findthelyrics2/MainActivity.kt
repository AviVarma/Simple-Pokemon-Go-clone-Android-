package com.example.findthelyrics2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ToggleButton

import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {

    /**
     * initialise the toolbar on top of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }

    /**
     * Attach the menu_home options buttons to the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    /**
     * Add functionality to the buttons given the menu layout file.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings ->{
                settingsIntent()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun settingsIntent(){
        var settingsIntent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }

    fun settingsButton(view: View){
        settingsIntent()
    }

    private fun mapIntent(){
        var mapIntent = Intent(applicationContext, MapsActivity::class.java)
        val toggle: ToggleButton = findViewById(R.id.toggleButton)
        mapIntent.putExtra("state", toggle.isChecked)
        startActivity(mapIntent)
    }

    fun playButton(view: View){
        mapIntent()
    }
}
