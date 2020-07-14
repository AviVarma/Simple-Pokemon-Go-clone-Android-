package com.example.findthelyrics2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_achievements.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class AchievementsActivity : AppCompatActivity() {

    /**
     * Songs that have been guessed correctly will be passed into the recycler view that will
     * by populated here.
     */
    private var collectedSongs = ArrayList<String>()
    private lateinit var achievementsPreferences: SharedPreferences

    /**
     * This method creates the activity and attaches to the required layout xml file.
     * The method also initialises the recycler view by attaching the correct adapter.
     * Param: savedInstanceState, if activity is changed, save state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)
        setSupportActionBar(toolbar)


        // initialise shared preferences
        achievementsPreferences = getSharedPreferences("achievements", Context.MODE_PRIVATE)
        achievementsPreferences.getStringSet("achievements", null)

        var achievementModelArrayList: ArrayList<AchievementsModel> = populateList()

        val recyclerView = findViewById<View>(R.id.recycler_achievements) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = AchievementsAdapter(achievementModelArrayList)
        recyclerView.adapter = mAdapter
    }

    /**
     * Adds songs to the recycler view
     * Method looks at the global array list for any collected songs that have been sent from the
     * intent "achievements" These new songs are added to the shared preference set so that previous
     * songs are also displayed.
     * return: Achievements list converted to AchievementsModek
     */
    private fun populateList(): ArrayList<AchievementsModel>{
        val list = ArrayList<AchievementsModel>()

        collectedSongs = intent.getStringArrayListExtra("achievements")
        val editor = achievementsPreferences.edit()

        var getSet = achievementsPreferences.getStringSet("achievements", null)
        var tempArray = getSet?.let { ArrayList<String>(it) }


        // if the game is ran first time preferences are null, hence arrays are initialized.
        try{
            tempArray!!.add(collectedSongs[0])

            var set = tempArray.toSet()

            editor.putStringSet("achievements", set)
            editor.apply()

            val lyricsCollectedSize = tempArray.size - 1

            for (i in 0..lyricsCollectedSize) {
                val achievementModel = AchievementsModel()
                achievementModel.setAchievements(tempArray[i])
                list.add(achievementModel)
            }
            return list
        }catch(e: Exception){
            val lyricsCollectedSize = collectedSongs.size - 1

            for (i in 0..lyricsCollectedSize) {
                val achievementModel = AchievementsModel()
                achievementModel.setAchievements(collectedSongs[i])
                list.add(achievementModel)
            }

            var set = collectedSongs.toSet()
            editor.putStringSet("achievements", set)
            editor.apply()
            return list
        }

    }
}
