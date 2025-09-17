package com.vibepro.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vibepro.app.databinding.ActivityMainBinding
import com.vibepro.app.ui.YouTubeActivity
import com.vibepro.app.ui.MusicListActivity
import com.vibepro.app.ui.PaywallActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnYouTube.setOnClickListener { startActivity(Intent(this, YouTubeActivity::class.java)) }
        binding.btnPresets.setOnClickListener { /* presets */ }
        binding.btnSettings.setOnClickListener { startActivity(Intent(this, com.vibepro.app.ui.SettingsActivity::class.java)) }
        binding.btnVisualizer.setOnClickListener { startActivity(Intent(this, com.vibepro.app.ui.VisualizerActivity::class.java)) }
        binding.btnEditor.setOnClickListener { startActivity(Intent(this, com.vibepro.app.ui.PresetEditorActivity::class.java)) }
        binding.btnPremium.setOnClickListener { startActivity(Intent(this, PaywallActivity::class.java)) }
    }
}
