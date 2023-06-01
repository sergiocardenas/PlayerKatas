package com.globant.playerkatas.presenter.service

import android.app.Notification
import android.content.Intent
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PlayerNotificationListener(private val service: PlayerService) : PlayerNotificationManager.NotificationListener {


    override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        service.startForeground(notificationId, notification)

    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        service.stopPlayer()
        service.baseContext.stopService(Intent(service.baseContext, PlayerService::class.java))
    }

}