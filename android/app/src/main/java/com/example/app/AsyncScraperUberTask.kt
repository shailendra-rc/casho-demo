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
  private var hasRides = false
  override fun processTask() {
    while (!hasRides) {
      this.context.runOnUiThread() {
        this.webView.evaluateJavascript(getJavascript()) { html ->
          if (html.toString().isNotEmpty() && html.contains("Past Trips")) {
            scrapData(html)

            val updatedScraperConfig = ScraperConfig()
            updatedScraperConfig.buttonClicks.add(scraperConfig.click)
            updatedScraperConfig.isAsync = scraperConfig.isAsync
            updatedScraperConfig.classes = scraperConfig.classes
            updatedScraperConfig.ids = scraperConfig.ids
            updatedScraperConfig.url = scraperConfig.url
            updatedScraperConfig.click = scraperConfig.click
            updatedScraperConfig.postLoginURL = scraperConfig.postLoginURL
            val scraperTask =
              ScraperTaskFactory.getScraperTask(
                ScraperTaskType.UBER,
                webView,
                updatedScraperConfig,
                scrapers,
                context
              )
            scraperTask?.runTask()

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
