package com.vibepro.app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.vibepro.app.R
import android.widget.Button
import android.widget.EditText

class YouTubeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        val et = findViewById<EditText>(R.id.et_video_id)
        val web = findViewById<WebView>(R.id.webview)
        val btnLoad = findViewById<Button>(R.id.btn_load)
        val btnOpen = findViewById<Button>(R.id.btn_open)
        web.settings.javaScriptEnabled = true
        web.webChromeClient = WebChromeClient()
        web.settings.domStorageEnabled = true
        web.settings.cacheMode = WebSettings.LOAD_DEFAULT
        btnLoad.setOnClickListener {
            var t = et.text.toString().trim()
            if (t.contains("youtube.com") || t.contains("youtu.be")) {
                val regex = ".*(?:v=|be/)([A-Za-z0-9_-]{11}).*".toRegex()
                val m = regex.find(t)
                t = m?.groups?.get(1)?.value ?: t
            }
            val html = "<html><body style='margin:0;padding:0;background:#000;'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/" + t + "?rel=0&autoplay=1' frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe></body></html>"
            web.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
        }
        btnOpen.setOnClickListener {
            val t = et.text.toString().trim()
            val videoId = if (t.contains("youtube.com") || t.contains("youtu.be")) {
                val regex = ".*(?:v=|be/)([A-Za-z0-9_-]{11}).*".toRegex()
                regex.find(t)?.groups?.get(1)?.value ?: t
            } else t
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId))
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId))
            try { startActivity(appIntent) } catch (ex: Exception) { startActivity(webIntent) }
        }
    }
}
