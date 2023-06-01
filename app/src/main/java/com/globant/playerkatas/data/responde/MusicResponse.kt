package com.globant.playerkatas.data.responde

data class MusicResponse(
    val id: String = "",
    val title: String = "",
    val preview: String = "",
    val artist: ArtistResponse = ArtistResponse(),
)
