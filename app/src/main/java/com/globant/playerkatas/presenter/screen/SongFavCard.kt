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
fun SongFavCard(
    musicItem: MusicState,
    onPlayClick: (MusicState) -> Unit
) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(8.dp))
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
                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.deezer_blue)),
            ){
                Text(
                    text = musicItem.title,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(100.dp)
                        .padding(8.dp),
                    color = Color.White
                )
                Text(
                    text = musicItem.artist,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(100.dp)
                        .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
                    color = Color.White
                )

                IconButton(
                    onClick = { onPlayClick(musicItem) },
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
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