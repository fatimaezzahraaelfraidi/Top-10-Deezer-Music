package com.example.toptenmusic

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toptenmusic.model.Album
import com.example.toptenmusic.model.Artist
import com.example.toptenmusic.model.Track
import com.example.toptenmusic.network.SpotifyApiService
import com.example.toptenmusic.network.TrackAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var artistEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        artistEditText = findViewById(R.id.artistEditText)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val artistName = artistEditText.text.toString()
            searchSongs(artistName)
        }
        recyclerView = findViewById(R.id.recyclerView)
        adapter = TrackAdapter(emptyList())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { track ->
            //log the track title for now
        }
        // Make API call to fetch all tracks when the app starts
        fetchData()
    }
    private fun updateTracks(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            adapter = TrackAdapter(tracks)
            recyclerView.adapter = adapter
        } else {
            println("No tracks to display.")
        }
    }
    private fun searchSongs(artistName: String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val request = Request.Builder()
            .url("https://deezerdevs-deezer.p.rapidapi.com/search?q=$artistName")
            .get()
            .addHeader("X-RapidAPI-Key", "4fcdfa3eadmsh7b18730b7d75637p1c924djsn4a8cfccc4726")
            .addHeader("X-RapidAPI-Host", "deezerdevs-deezer.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                   println("Error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseData = it.string()
                    val tracks = parseTracks(responseData)
                    runOnUiThread {
                        updateTracks(tracks)
                    }
                }
            }
        })
    }

    private fun parseTracks(responseData: String): List<Track> {
        val tracksList = mutableListOf<Track>()

        try {
            val jsonObject = JSONObject(responseData)
            val data = jsonObject.optJSONArray("data")

            for (i in 0 until minOf(data.length(), 10)) {
                val trackObject = data.getJSONObject(i)
                val title = trackObject.getString("title")
                val albumtitle = trackObject.getJSONObject("album").getString("title")
                val albumcoverM = trackObject.getJSONObject("album").getString("cover_medium")
                val albumcoverB = trackObject.getJSONObject("album").getString("cover_big")
                val artistname = trackObject.getJSONObject("artist").getString("name")
                val artistpictureB = trackObject.getJSONObject("artist").getString("picture_big")
                val duration = trackObject.getString("duration")
                val rank = trackObject.getString("rank")
                val artist = Artist(artistname,artistpictureB)
                val album = Album(albumtitle,albumcoverM,albumcoverB)
                val track = Track(title,artist,album,duration.toInt(), rank.toInt())
                tracksList.add(track)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tracksList.sortedBy { it: Track -> it.rank }.reversed()
    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(SpotifyApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getTopTracks()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.data ?: emptyList()
                    adapter = TrackAdapter(tracks.sortedBy { it: Track -> it.rank }.reversed())
                    recyclerView.adapter = adapter
                } else {
                    // Handle error
                    Log.e("API Error", response.message())
                }
            }
        }
    }

}





