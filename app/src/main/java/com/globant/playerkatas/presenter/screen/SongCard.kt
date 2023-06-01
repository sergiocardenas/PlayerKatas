package com.globant.playerkatas.presenter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.globant.playerkatas.R
import com.globant.playerkatas.presenter.state.MusicState

@Composable
fun SongCard(
    index: Int,
    musicItem: MusicState,
    onFavClick: (Int) -> Unit,
    onPlayClick: (MusicState) -> Unit
) {
    var isFav by remember { mutableStateOf(musicItem.fav) }

    Box(
        modifier = Modifier
            .width(240.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(musicItem.picture),
            contentDescription = musicItem.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(id = R.color.deezer_blue)),
            ){
                Text(
                    text = musicItem.title,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(160.dp)
                        .padding(8.dp),
                    color = Color.White
                )
                Text(
                    text = musicItem.artist,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(160.dp)
                        .padding(top = 24.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                    color = Color.White
                )


                IconButton(
                    onClick = {
                        onFavClick(index)
                        isFav = !isFav
                    },
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .align(Alignment.BottomStart)
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Play",
                        tint = if (isFav)
                                colorResource(id = R.color.deezer_white)
                            else
                                colorResource(id = R.color.deezer_background)
                    )
                }

                IconButton(
                    onClick = { onPlayClick(musicItem) },
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd)
                        .size(48.dp)
                        .background(colorResource(id = R.color.deezer_white), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}