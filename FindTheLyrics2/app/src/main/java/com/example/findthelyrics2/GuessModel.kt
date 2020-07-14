package com.example.findthelyrics2

class GuessModel {
    var lyric: String? = null

    /**
     * Guess object for recycler view adapter
     */

    fun getLyrics(): String {
        return lyric.toString()
    }

    fun setlyrics(name: String) {
        this.lyric = name
    }
}