package com.globant.playerkatas.presenter.state

data class MusicState(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val preview: String = "",
    val picture: String = "",
    val fav: Boolean = false,
)
