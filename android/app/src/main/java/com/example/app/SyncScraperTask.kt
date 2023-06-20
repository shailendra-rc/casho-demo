package com.example.app

import android.app.Activity
import android.webkit.WebView
import java.util.LinkedList

class SyncScraperTask(
  override var webView: WebView,
  override var scraperConfig: ScraperConfig,
  override var scrapers: LinkedList<ScraperConfig>,
  override var context: Activity
) : ScraperTask() {

  override fun runTask() {
    webView.evaluateJavascript(getJavascript()) { html ->
      scrapData(html)
    }
  }
}

