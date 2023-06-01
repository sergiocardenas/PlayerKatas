package com.globant.playerkatas.presenter.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.globant.playerkatas.presenter.screen.PlayerScreen
import com.globant.playerkatas.presenter.service.PlayerService
import com.globant.playerkatas.presenter.viemodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var playerViewModel: PlayerViewModel
    private var isBind = false

    private val playerConnection = object  : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.ServiceBinder
            playerViewModel.setPlayer(binder.getPlayerService().getPlayer())
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        val composeView = ComposeView(this)
        composeView.setContent {
            PlayerScreen(playerViewModel)
        }

        setContentView(composeView)

        val playerServiceIntent = Intent(this, PlayerService::class.java)
        onBind(playerServiceIntent)
        //startService(playerServiceIntent)
    }

    private fun onBind(playerServiceIntent : Intent){
        bindService(playerServiceIntent, playerConnection, Context.BIND_AUTO_CREATE)
        ContextCompat.startForegroundService(this, playerServiceIntent)
        isBind = true
    }

    override fun onDestroy() {
        super.onDestroy()
        unBind()
    }

    private fun unBind(){
        if(isBind){
            unbindService(playerConnection)
            isBind = false
        }
    }
}