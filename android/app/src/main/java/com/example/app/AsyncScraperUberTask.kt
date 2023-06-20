package com.example.app

import android.app.Activity
import android.webkit.WebView
import java.util.LinkedList

class AsyncScraperUberTask(
  override var webView: WebView,
  override var scraperConfig: ScraperConfig,
  override var scrapers: LinkedList<ScraperConfig>,
  override var context: Activity
) : AsyncScraperTask(webView, scraperConfig, scrapers, context) {
  var hasRides = false
  override fun processTask() {
    while (!hasRides) {
      this.context.runOnUiThread() {
        this.webView.evaluateJavascript(getJavascript()) { html ->
          if (html.toString().isNotEmpty() && html.contains("Past Trips")) {
            scrapData(html)
            this.hasRides = true
          }
        }
      }
      Thread.sleep(3000)
    }
  }

  override fun runTask() {
    Thread(this).start()
  }
}
