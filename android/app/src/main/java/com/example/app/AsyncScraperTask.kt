package com.example.app

import android.app.Activity
import android.webkit.WebView
import java.util.LinkedList

abstract class AsyncScraperTask(
  override var webView: WebView,
  override var scraperConfig: ScraperConfig,
  override var scrapers: LinkedList<ScraperConfig>,
  override var context: Activity
) : ScraperTask(), Runnable {
  override fun run() {
    processTask()
  }

  abstract fun  processTask()

  override fun runTask() {
    Thread(this).start()
  }
}
