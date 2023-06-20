package com.example.app

import android.app.Activity
import android.webkit.WebView
import java.util.LinkedList

enum class ScraperTaskType {
  ASYNC,
  SYNC
}
class ScraperTaskFactory {
  companion object {
    fun getScraperTask(type: ScraperTaskType, webView: WebView, scraperConfig: ScraperConfig, scrapers: LinkedList<ScraperConfig>, context: Activity): IScraperTask? {
      return if (type == ScraperTaskType.ASYNC) {
        AsyncScraperTask(webView, scraperConfig, scrapers, context)
      } else if (type == ScraperTaskType.SYNC){
        SyncScraperTask(webView, scraperConfig, scrapers, context)
      } else {
        null
      }
    }
  }
}
