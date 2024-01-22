package com.example.toptenmusic.model

data class Track(
    val title: String,
    val artist: Artist,
    val album: Album,
    val duration: Int,
    val rank: Int
)