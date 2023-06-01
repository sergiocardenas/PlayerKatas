package com.globant.playerkatas.presenter.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.globant.playerkatas.presenter.screen.SongFavCard
import com.globant.playerkatas.presenter.state.MusicState
import com.globant.playerkatas.presenter.viemodel.PlayerViewModel

@Composable
fun FavoriteScreen(
    viewModel: PlayerViewModel,
    navController: NavController
) {
    val listFavState = viewModel.listFav.collectAsState()

    val goToHome: () -> Unit = {
        navController.navigate("home_screen")
    }

    val playSong: (MusicState) -> Unit = { song ->
        if(viewModel.currentPlayer.value != null)
            viewModel.setNewMusic(song)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Favorites",
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            color = Color.White
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Display the play/pause button
            IconButton(
                onClick = {
                    goToHome()
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
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go to Favorites",
                    tint = colorResource(id = R.color.deezer_background)
                )
            }

            Text(
                text = "Go back to home",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.White
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(listFavState.value) { song ->
                SongFavCard(
                    musicItem = song,
                    onPlayClick = playSong
                )
            }
        }
    }


}