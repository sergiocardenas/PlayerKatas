package com.globant.playerkatas.presenter.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.globant.playerkatas.presenter.activity.MainActivity
import com.globant.playerkatas.R
import com.globant.playerkatas.presenter.service.ServiceConstants.CHANNEL_ID
import com.globant.playerkatas.presenter.service.ServiceConstants.NOTIFICATION_ID
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH


class PlayerService : Service() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: PlayerNotificationManager
    private var serviceBinder =  object : ServiceBinder() {
        override fun getPlayerService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onBind(intent: Intent): IBinder{
        return serviceBinder
    }

    abstract class ServiceBinder(): Binder() {
        abstract fun getPlayerService(): PlayerService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()

        val audioAtt = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        exoPlayer.setAudioAttributes(audioAtt, true)

        // Initialize the MediaSession
        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession.setPlaybackState(createPlaybackState())
        mediaSession.setCallback(createMediaSessionCallback())

        // Initialize the MediaSessionConnector
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)

        notificationManager = initNotificationManager()


        val channel = NotificationChannel(
            CHANNEL_ID,
            "Katas player cannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager =
            baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_continue_playing)
            .setContentTitle("Katas")
            .setContentText("Katas Player is running").build()
        startForeground(NOTIFICATION_ID, notification)
    }


    private fun initNotificationManager(): PlayerNotificationManager{

        val playerNotificationManager = PlayerNotificationManager.Builder(
            baseContext, NOTIFICATION_ID, CHANNEL_ID
        ).setChannelImportance(IMPORTANCE_HIGH)
        .setSmallIconResourceId(R.drawable.ic_continue_playing)
        .setNextActionIconResourceId(R.drawable.ic_continue_playing)
        .setPreviousActionIconResourceId(R.drawable.ic_previous_playing)
        .setChannelNameResourceId(R.string.app_name)
        .setMediaDescriptionAdapter(
            object : PlayerNotificationManager.MediaDescriptionAdapter{
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    var title = "Katas Player"
                    player.currentMediaItem?.let {
                        title = (it.mediaMetadata.title ?: "Katas Player") as String
                    }
                    return title
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    val openAppIntent = Intent(applicationContext, MainActivity::class.java)

                    return PendingIntent.getActivity(
                        applicationContext, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE)
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    var content = "Katas Player"
                    player.currentMediaItem?.let {
                        content = (it.mediaMetadata.artist ?: "Katas Player") as String
                    }
                    return content
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return BitmapFactory.decodeResource(baseContext.resources, R.drawable.ic_continue_playing)
                }

            }
        ).setNotificationListener(PlayerNotificationListener(this)).build()

        playerNotificationManager.apply {
            setMediaSessionToken(mediaSession.sessionToken)
            setPlayer(exoPlayer)
            setPriority(PRIORITY_MAX)
        }
        return playerNotificationManager
    }


    private fun createPlaybackState(): PlaybackStateCompat {
        return PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_STOP)
            .build()
    }

    private fun createMediaSessionCallback(): MediaSessionCompat.Callback {
        return object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                exoPlayer.play()
            }

            override fun onPause() {
                exoPlayer.pause()
            }

            override fun onStop() {
                exoPlayer.stop()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(exoPlayer.isPlaying) exoPlayer.stop()
        notificationManager.setPlayer(null)
        exoPlayer.release()
        mediaSession.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }



    fun getPlayer() = exoPlayer

    fun stopPlayer(){
        exoPlayer.stop()
    }
}