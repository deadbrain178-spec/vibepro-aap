package com.vibepro.app.player
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.audio.AudioAttributes

class PlayerService : Service() {
    private lateinit var player: ExoPlayer
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(AudioAttributes.Builder().setContentType(com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC).build(), true)
        }
        startForeground(1, buildNotification())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val uris = intent?.getParcelableArrayListExtra<android.net.Uri>("uris") ?: arrayListOf()
        if (uris.isNotEmpty()) {
            player.setMediaItems(uris.map { MediaItem.fromUri(it) })
            player.prepare(); player.play()
        }
        if (intent?.getBooleanExtra("hz432", false) == true) { player.playbackParameters = PlaybackParameters(1f, 432f/440f) }
        return START_STICKY
    }
    override fun onDestroy() { player.release(); super.onDestroy() }
    override fun onBind(p0: Intent?): IBinder? = null
    private fun buildNotification(): Notification = NotificationCompat.Builder(this, "media").setContentTitle("VibePro — odtwarzanie").setSmallIcon(android.R.drawable.ic_media_play).setOngoing(true).build()
    companion object {
        fun playIntent(ctx: Context, uris: ArrayList<android.net.Uri>, hz432:Boolean=false): Intent = Intent(ctx, PlayerService::class.java).apply { putParcelableArrayListExtra("uris", uris); putExtra("hz432", hz432) }
    }
}
