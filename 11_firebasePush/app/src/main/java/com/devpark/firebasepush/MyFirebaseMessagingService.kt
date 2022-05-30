package com.devpark.firebasepush

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.NotificationParams
import com.google.firebase.messaging.RemoteMessage
import java.nio.channels.Channel

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * 다음과 같은 경우에 등록 토큰이 변경될 수 있습니다.
     * 새 기기에서 앱 복원
     * 사용자가 앱 삭제/재설치
     * 사용자가 앱 데이터 소거
     *
     * 토큰이 언제든지 변경 될 수 있기 때문에 실제 라이브 서비스를 운영할 때는
     * onNewToken를 오버라이딩 해서 이 토큰이 갱신 될 때마다 서버에다 해당 토큰을 갱신해 주어야 한다.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    /**
     * Firebase에서 넘어오는 메시지 처리
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val type = remoteMessage.data["type"]?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return


        NotificationManagerCompat.from(this).notify(type.id, createNotification(type, title, message))

    }

    private fun createNotification(type: NotificationType, title: String?, message: String?): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, type.id, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNER_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "\uD83E\uDD17\uD83D\uDE02\uD83D\uDE02\uD83C\uDF77\uD83D\uDE4F\uD83D\uDE09\uD83D\uDC4D✅\uD83E\uDD14\uD83D\uDE43\uD83D\uDCE2\uD83D\uDE42\uD83D\uDE04✔️\uD83D\uDC95÷☀️🤗😂😂🍷🙏😉👍✅🤔🙃📢🙂😄✔️💕÷☀️\uD83E\uDD17\uD83D\uDE02\uD83D\uDE02\uD83C\uDF77\uD83D\uDE4F\uD83D\uDE09\uD83D\uDC4D✅\uD83E\uDD14\uD83D\uDE43\uD83D\uDCE2\uD83D\uDE42\uD83D\uDE04✔️\uD83D\uDC95÷☀️"
                    )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }

        return notificationBuilder.build()

    }


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNER_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }//end of createNotificationChannel

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNER_ID = "Channel Id"
    }
}