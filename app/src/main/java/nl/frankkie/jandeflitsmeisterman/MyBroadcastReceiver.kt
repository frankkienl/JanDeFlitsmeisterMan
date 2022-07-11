package nl.frankkie.jandeflitsmeisterman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, broadcastIntent: Intent?) {
        if (context != null) {
            val status =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("status", false)
            if (!status) return
        }
        broadcastIntent?.let { safeBroadcastIntent ->
            val intentAction = safeBroadcastIntent.action
            if ("jandeflitsmeisterman" == intentAction) {
                Toast.makeText(
                    context,
                    "JanDeFlitsmeisterMan: TEST",
                    Toast.LENGTH_LONG
                ).show()
                startFlitsmeister(context)
            }
            if ("android.intent.action.BOOT_COMPLETED" == intentAction) {
                startBackgroundService(context)
            }
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intentAction) {
                Toast.makeText(
                    context,
                    "JanDeFlitsmeisterMan: ACTION_AUDIO_BECOMING_NOISY",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (AudioManager.ACTION_HEADSET_PLUG == intentAction) {
                val state = safeBroadcastIntent.getIntExtra("state", -1)
                val name = safeBroadcastIntent.getStringExtra("name")
                val microphone = safeBroadcastIntent.getIntExtra("microphone", -1)
                Toast.makeText(
                    context,
                    "JanDeFlitsmeisterMan: ACTION_HEADSET_PLUG ($state, $name, $microphone)",
                    Toast.LENGTH_LONG
                ).show()

                if (state == 1) {
                    //Headset plugged in
                    startFlitsmeister(context)
                }
            }
        }
    }

    companion object {
        public fun startBackgroundService(context: Context?) {
            context?.let { safeContext ->
                val serviceIntent = Intent(safeContext, MyBackgroundService::class.java)
                safeContext.startService(serviceIntent)
            }
        }

        public fun startFlitsmeister(context: Context?) {
            val flitsmeisterIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("flitsmeister://start"),
                )
            flitsmeisterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(flitsmeisterIntent)
        }
    }

}