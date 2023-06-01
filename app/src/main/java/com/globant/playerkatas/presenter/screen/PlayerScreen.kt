package com.globant.playerkatas.presenter.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.globant.playerkatas.R
import com.globant.playerkatas.presenter.service.FavoriteScreen
import com.globant.playerkatas.presenter.service.HomeScreen
import com.globant.playerkatas.presenter.viemodel.PlayerViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel
) {
    val viewModelIsPlaying = viewModel.isPlaying.collectAsState()

    var isPlaying by remember { mutableStateOf(false) }

    isPlaying = viewModelIsPlaying.value

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            if(viewModel.currentPlayer.value != null)
                viewModel.currentPlayer.value!!.play()
        } else {
            if(viewModel.currentPlayer.value != null)
                viewModel.currentPlayer.value!!.pause()
        }
    }

    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(colorResource(id = R.color.deezer_grey)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                // Display previous
                Image(
                    painter = painterResource(id = R.drawable.ic_previous_playing),
                    contentDescription = "Previous",
                    modifier = Modifier
                        .background(colorResource(id = R.color.deezer_white), shape = CircleShape)
                        .size(48.dp)
                        .clickable {
                            viewModel.moveSong(false)
                        }
                )

                // Display the play/pause button
                IconButton(
                    onClick = {
                        viewModel.playControl(!isPlaying)
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .size(48.dp)
                        .border(4.dp, colorResource(id = R.color.black), CircleShape)
                        .background(colorResource(id = R.color.deezer_white), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Clear else Icons.Filled.PlayArrow,
                        contentDescription = "Play/Pause"
                    )
                }

                // Display previous
                Image(
                    painter = painterResource(id = R.drawable.ic_continue_playing),
                    contentDescription = "Next",
                    modifier = Modifier
                        .background(colorResource(id = R.color.deezer_white), shape = CircleShape)
                        .size(48.dp)
                        .clickable {
                            viewModel.moveSong(true)
                        }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.background(colorResource(id = R.color.deezer_background)),
        ){

            // Space for navigation
            Box(modifier = Modifier.weight(1f)) {
                // This is where the screens defined in NavHost will be displayed
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen") { HomeScreen(viewModel, navController) }
                    composable("favorite_screen") { FavoriteScreen(viewModel, navController) }
                    // Add more destinations as needed
                }
            }

        }
    }
}
