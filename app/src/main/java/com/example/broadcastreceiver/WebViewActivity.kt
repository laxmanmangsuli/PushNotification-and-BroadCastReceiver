package com.example.broadcastreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView = findViewById<WebView>(R.id.webView)
        val url = intent.getStringExtra("url")

        if (url != null) {
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
        }
    }
}
