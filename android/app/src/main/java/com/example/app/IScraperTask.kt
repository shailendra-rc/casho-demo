package com.example.app

import android.app.Activity
import android.webkit.WebView
import java.util.LinkedList

interface IScraperTask {
  var webView: WebView
  var scraperConfig: ScraperConfig
  var scrapers: LinkedList<ScraperConfig>
  var context: Activity
  fun runTask();
}
