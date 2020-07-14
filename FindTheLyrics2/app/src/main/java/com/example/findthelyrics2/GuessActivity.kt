package com.example.findthelyrics2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_guess.*
import kotlinx.android.synthetic.main.content_guess.*


class GuessActivity : AppCompatActivity() {

    private var collectedLyrics = ArrayList<String>()
    private var collectedSong = ""
    private lateinit var streakPreference : SharedPreferences
    private lateinit var pointsPreference: SharedPreferences

    /**
     * Initialise the page by attaching to the activity view xml.
     * Initialise the shared preferences to store points and streaks score.
     * Initialise the recycler view for the collected lyrics.
     * Set up the give up button and corresponding intent required for functionality
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess)
        setSupportActionBar(toolbar)

        streakPreference = getSharedPreferences("streak", Context.MODE_PRIVATE)
        pointsPreference = getSharedPreferences("points", Context.MODE_PRIVATE)

        val streaksText = streakPreference.getInt("streak", 0).toString()

        val streakTextView = findViewById<TextView>(R.id.streak_box)
        streakTextView.text = streaksText

        pointsPreference.getInt("points",0)

        var guessModelArrayList: ArrayList<GuessModel> = populateList()

        val recyclerView = findViewById<View>(R.id.recycler_lyrics) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = GuessAdapter(guessModelArrayList)
        recyclerView.adapter = mAdapter

        val button = findViewById<Button>(R.id.give_up_button)
        button.setOnClickListener {
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            streakPreference.edit().putInt("streak",0)
            streakPreference.edit().apply()
        }
    }

    /**
     * Add functionality to the flame icon and show a snackbar with the current stats for the p]ayer
     */
    fun streaksButton(view: View){
        val streaksText = streakPreference.getInt("streak", 0).toString()
        val pointsText = pointsPreference.getInt("points",0).toString()

        view.setOnClickListener{
            Snackbar.make(it, "streak: $streaksText Points: $pointsText",
                Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Add an options menu to the toolbar with the menu_guess layout for buttons.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_guess, menu)
        return true
    }

    /**
     * Add functionality for the buttons provided by the menu_guess.xml
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_achievements -> {
                achievementsIntent()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Change activity to achievements if the song has been guessed correctly.
     * If condition not satisfied notify the user with snackbar.
     */
    private fun achievementsIntent(){
        if(collectedSong.isEmpty()){
            Snackbar.make(findViewById(R.id.guess_view), getString(R.string.guess_first_toast),
                Snackbar.LENGTH_SHORT).show()
        } else{
            var songArray = ArrayList<String>()
            collectedSong = collectedSong + ")"
            collectedSong = collectedSong.replace("_", " ")
            songArray.add(collectedSong)
            var achievementsIntent = Intent(applicationContext, AchievementsActivity::class.java)
            achievementsIntent.putExtra("achievements", songArray)
            startActivity(achievementsIntent)
        }
    }

    /**
     * Check if the guess given is valid by taking the file name from the previous activity
     * extract the artist name and the song name to check if the guess given is correct.
     * Update the streaks and points for the user depending on the user's guess.
     */
    fun imageButton(view: View){
        val pointsIncrement = 5

        var markerNumber = intent.getIntExtra("markerNumber", 6)

        // pre process the filename intent so it can be compared with user's input.
        var fileName = intent.getStringExtra("fileName")
        fileName = fileName.substringBefore(")")

        var artists = fileName.substringBefore("(")
        artists.toLowerCase()
        var artist = artists.split("__").toTypedArray()
        var size = artist.size-1
        for(i in 0..size){
            artist[i] = artist[i].replace("_", " ")
            if(artist[i].startsWith(" ")){
                artist[i] = artist[i].removePrefix(" ")
            }
        }

        var song = fileName.substringAfter("(")
        song = song.substringBefore(")")
        song = song.replace("_", " ")

        Log.i("filename", song)

        val guess: String = input_guess.text.toString().toLowerCase()

        val streakEditor = streakPreference.edit()
        val pointsEditor = pointsPreference.edit()

        // logic to compare song and artists names with user's guess.
        if(guess.equals(song) || artist.contains(guess)){
            Snackbar.make(view, getString(R.string.response_correct), Snackbar.LENGTH_LONG).show()
            var streakPoints = streakPreference.getInt("streak",0)
            streakPoints ++
            streakEditor.putInt("streak",streakPoints)
            streakEditor.apply()
            var points = pointsPreference.getInt("points", 0)
            points += pointsIncrement
            pointsEditor.putInt("points", points)
            pointsEditor.apply()
            collectedSong = fileName
            collectedLyrics.clear()
        } else{
            Snackbar.make(view, getString(R.string.response_wrong), Snackbar.LENGTH_LONG).show()
            streakEditor.putInt("streak",0)
            streakEditor.apply()
            var points = pointsPreference.getInt("points", 0)
            points -= pointsIncrement
            pointsEditor.putInt("points", points)
            pointsEditor.apply()
        }

        // update the streaks.
        val box = findViewById<TextView>(R.id.streak_box)
        box.text = streakPreference.getInt("streak",0).toString()
    }

    /**
     * take the array list full of collected lyrics anc convert it into the required model for the
     * recycler view population process.
     * Return: converted list.
     */
    private fun populateList(): ArrayList<GuessModel> {
        val list = ArrayList<GuessModel>()

        collectedLyrics.clear()
        collectedLyrics = intent.getStringArrayListExtra("lyricsCollected")

        val lyricsCollectedSize = collectedLyrics.size - 1

        for (i in 0..lyricsCollectedSize) {
            val guessModel = GuessModel()
            guessModel.setlyrics(collectedLyrics[i])
            list.add(guessModel)
        }
        return list
    }

}