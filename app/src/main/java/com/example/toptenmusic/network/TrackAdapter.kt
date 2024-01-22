package com.example.toptenmusic.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.toptenmusic.R
import com.example.toptenmusic.model.Track

class TrackAdapter(private val tracks: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    private var onItemClickListener: ((Track) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val artistTextView: TextView = view.findViewById(R.id.artistTextView)
        val durationTextView: TextView = view.findViewById(R.id.durationTextView)
        val coverImageView: ImageView = view.findViewById(R.id.coverImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = tracks[position]
        holder.titleTextView.text = track.title
        holder.artistTextView.text = track.artist.name
        holder.durationTextView.text = track.duration.toString()+" min   " + track.rank.toString()
        // Load and display the image using Glide library
        Glide.with(holder.itemView)
            .load(track.album.cover_medium)
            .into(holder.coverImageView)

        // Set click listener for the "View Details" button


    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setOnItemClickListener(listener: (Track) -> Unit) {
        onItemClickListener = listener
    }
}
