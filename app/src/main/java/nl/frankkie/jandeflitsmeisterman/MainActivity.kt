package nl.frankkie.jandeflitsmeisterman

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import nl.frankkie.jandeflitsmeisterman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.mainBtnOn.setOnClickListener {
            setStatus(true)
            MyBackgroundService.startReceiver(this)
            updateStatusTv(binding)
        }
        binding.mainBtnOff.setOnClickListener {
            setStatus(false)
            MyBackgroundService.stopReceiver(this)
            updateStatusTv(binding)
        }

        updateStatusTv(binding)
    }

    private fun setStatus(status: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putBoolean("status", status).apply()
    }

    private fun updateStatusTv(binding: ActivityMainBinding) {
        val status = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("status", false)
        binding.mainStatusTv.text =
            "Status: $status - ${MyBackgroundService.Companion.myBroadcastReceiver != null}"
    }

}