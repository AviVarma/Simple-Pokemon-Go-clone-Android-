package com.example.findthelyrics2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class AchievementsAdapter (private val achModelArrayList: MutableList<AchievementsModel>) : RecyclerView.Adapter<AchievementsAdapter.ViewHolder>() {

    /**
     * Companion class required for recycler adapter
     */
    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var txtMsg: TextView

        init {
             txtMsg = layout.findViewById<View>(R.id.textView2) as TextView
        }
    }

    /**
     * Initialise the adapter and attach to the recycler view xml which will be attached to the
     * main activity.
     * Return: viewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.recycler_view_achievements, parent, false)

        return ViewHolder(v)
    }

    /**
     * Adds an element of type AchievemntsModel into a row
     * The row is intractable through url intent which will take user to the song's spotify.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = achModelArrayList[position]

        holder.txtMsg.text = info.getAchievements()
        var mySong = info.getAchievements().substringAfter("(")
        mySong = mySong.substringBefore(")") as String
        holder.txtMsg.setOnClickListener {v ->
            val uris = Uri.parse(songURL(mySong))
            var intent = Intent(Intent.ACTION_VIEW, uris)
            v.context.startActivity(intent)
            var snack = Snackbar.make(v, info.getAchievements(), Snackbar.LENGTH_LONG)
                .show()

        }
    }

    /**
     * List of all songs spotify links that can be used to mek the recycler view intractable.
     * Return: queried URL.
     */
    private fun songURL(input: String): String{
        var uri = when(input){
            "ladbroke grove" -> "https://open.spotify.com/track/7b4ky1LlQLFhXHmhZkFgqV"
            "taste make it shake" -> "https://open.spotify.com/track/1xPpYbCaJWonL7IFXPDjON"
            "don't call me angle" -> "https://open.spotify.com/track/6zegtH6XXd2PDPLvy1Y0n2"
            "professor x" -> "https://open.spotify.com/track/5wfyoR8XOkw45QRtFOoxyi"
            "outnumbered" -> "https://open.spotify.com/track/6UjZ2Yx2g2a52XxiA8ONxZh"
            "3 nights" -> "https://open.spotify.com/track/1tNJrcVe6gwLEiZCtprs1u"
            "take me back to london" -> "https://open.spotify.com/track/1AI7UPw3fgwAFkvAlZWhE0"
            "both" -> "https://open.spotify.com/track/15nKLSvslk7jntDkxIdYmq"
            "sorry" -> "https://open.spotify.com/track/6y1UtRcHQU07aUs3oxZ8Yn"
            "be honest" -> "https://open.spotify.com/track/5pAbCxt9e3f81lOmjIXwzd"
            "higher love" -> "https://open.spotify.com/track/6oJ6le65B3SEqPwMRNXWjY"
            "ransom" -> "https://open.spotify.com/track/1lOe9qE0vR9zwWQAOk6CoO"
            "circles" -> "https://open.spotify.com/track/21jGcNKet2qwijlDFuPiPb"
            "goodbyes" -> "https://open.spotify.com/track/0t3ZvGKlmYmVsDzBJAXK8C?si=XlYzWSHpQT-Qhfj7ZLXFKg"
            "ride it" -> "https://open.spotify.com/track/2tnVG71enUj33Ic2nFN6kZ"
            "post malone" -> "https://open.spotify.com/track/0qc4QlcCxVTGyShurEv1UU"
            "how do you sleep" -> "https://open.spotify.com/track/6b2RcmUt1g9N9mQ3CbjX2Y"
            "senorita" -> "https://open.spotify.com/track/0TK2YIli7K1leLovkQiNik"
            "dance monkey" -> "https://open.spotify.com/track/2XU0oxnq2qxCpomAAuJY8K"
            "strike a pose" -> "https://open.spotify.com/track/23GvTfcGK454ppLsts3W44"
            "like a rolling stone" -> "https://open.spotify.com/track/3AhXZa8sUQht0UEdBJgpGc"
            "your song" -> "https://open.spotify.com/track/38zsOOcu31XbbYj9BIPUF1"
            "sweet child o mine" -> "https://open.spotify.com/track/7o2CTH4ctstm8TNelqjb51"
            "imagine" -> "https://open.spotify.com/track/7pKfPomDEeI4TPT6EOYjn9"
            "over the rainbow"-> "https://open.spotify.com/track/4yDjzVhXig9tfO7Zv46FE8"
            "stairway to heaven" -> "https://open.spotify.com/track/5CQ30WqJwcep0pYcV4AMNc"
            "billie jean" -> "https://open.spotify.com/track/5ChkMS8OtdzJeqyybCc9R5"
            "smells like teen spirit" -> "https://open.spotify.com/track/1f3yAtsJtY87CTmM8RLnxf"
            "live forever" -> "https://open.spotify.com/track/726Bm6HoMMOwrJlxqbfO45"
            "bohemian rhapsody" -> "https://open.spotify.com/track/3z8h0TU7ReDPLIbEnYhWZb"
            "i can't get no satisfaction" -> "https://open.spotify.com/track/2PzU4IB8Dr6mxV3lHuaG34"
            "god save the queen" -> "https://open.spotify.com/track/2mHchPRtQWet3iIS3jANr1"
            "hey jude" -> "https://open.spotify.com/track/1eT2CjXwFXNx6oY5ydvzKU"
            "london calling" -> "https://open.spotify.com/track/5jzma6gCzYtKB1DbEwFZKH"
            "hotel california" -> "https://open.spotify.com/track/40riOy7x9W7GXjyGp4pjAv"
            "waterloo sunset" -> "https://open.spotify.com/track/3Uc7SXgbFRbCJGic2brAxh"
            "one" -> "https://open.spotify.com/track/3G69vJMWsX6ZohTykad2AU"
            "i will always love you" -> "https://open.spotify.com/track/4eHbdreAnSOrDDsFfc4Fpm"
            else -> "https://google.co.uk"
        }
        return uri
    }

    override fun getItemCount(): Int {
        return achModelArrayList.size
    }
}