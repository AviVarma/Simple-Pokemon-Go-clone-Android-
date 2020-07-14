package com.example.findthelyrics2

class AchievementsModel{

    /**
     * Achievements object for recycler view adapter
     */

    var achievement: String? = null

    fun getAchievements(): String {
        return achievement.toString()
    }

    fun setAchievements(name: String) {
        this.achievement = name
    }
}