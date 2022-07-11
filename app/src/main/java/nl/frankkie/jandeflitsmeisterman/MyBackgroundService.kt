package nl.frankkie.jandeflitsmeisterman

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.IBinder
import android.preference.PreferenceManager

class MyBackgroundService : Service() {

    override fun onBind(binderIntent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val status =
            PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("status", false)
        if (status) {
            startReceiver(this)
            return START_STICKY
        } else {
            return START_NOT_STICKY
        }
    }

    override fun onDestroy() {
        stopReceiver(this)
    }

    companion object {

        val intentFilter = IntentFilter(AudioManager.ACTION_HEADSET_PLUG)
        var myBroadcastReceiver: MyBroadcastReceiver? = null

        public fun startReceiver(context: Context?) {
            myBroadcastReceiver = MyBroadcastReceiver()
            context?.registerReceiver(myBroadcastReceiver, intentFilter)
        }

        public fun stopReceiver(context: Context?) {
            myBroadcastReceiver?.let {
                try {
                    context?.unregisterReceiver(it)
                } catch (e: Exception) {
                    //ignore
                }
            }
        }
    }
}