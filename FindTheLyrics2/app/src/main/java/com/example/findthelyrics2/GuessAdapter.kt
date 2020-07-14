package com.example.findthelyrics2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class GuessAdapter (private val guessModelArrayList: MutableList<GuessModel>) : RecyclerView.Adapter<GuessAdapter.ViewHolder>() {

    /**
     * Companion class required for recycler adapter
     */
    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var txtMsg: TextView

        init {
            txtMsg = layout.findViewById<View>(R.id.textView1) as TextView
        }
    }

    /**
     * Initialise the adapter and attach to the recycler view xml which will be attached to the
     * main activity.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.recycler_view_guess, parent, false)

        return ViewHolder(v)
    }

    /**
     * Adds an element of type GuessModel into a row
     * The row is intractable through url intent which will take user to the song's spotify.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = guessModelArrayList[position]

        holder.txtMsg.text = info.getLyrics()
        holder.txtMsg.setOnClickListener {v ->
            Snackbar.make(v, info.getLyrics(), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return guessModelArrayList.size
    }
}