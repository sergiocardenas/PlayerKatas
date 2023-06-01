package com.globant.playerkatas.presenter.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.globant.playerkatas.R
import com.globant.playerkatas.presenter.screen.SongCard
import com.globant.playerkatas.presenter.state.MusicState
import com.globant.playerkatas.presenter.viemodel.PlayerViewModel

@Composable
fun HomeScreen(
    viewModel: PlayerViewModel,
    navController: NavController
) {

    val listState = viewModel.list.collectAsState()

    val goToFav: () -> Unit = {
        navController.navigate("favorite_screen")
    }

    val playSong: (MusicState) -> Unit = { song ->
        if(viewModel.currentPlayer.value != null)
            viewModel.setNewMusic(song)
    }

    val addFav: (Int) -> Unit = { indx ->
        viewModel.manageFav(indx)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Top Trending",
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.White
        )

        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(listState.value) { indx, song ->
                SongCard(
                    index = indx,
                    musicItem = song,
                    onFavClick =  addFav,
                    onPlayClick = playSong
                )
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Display the play/pause button
            IconButton(
                onClick = {
                    goToFav()
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterVertically)
                    .size(48.dp)
                    .background(
                        colorResource(id = R.color.deezer_blue),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Go to Favorites",
                    tint = colorResource(id = R.color.deezer_background)
                )
            }

            Text(
                text = "Favorites",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.White
            )
        }
    }

}