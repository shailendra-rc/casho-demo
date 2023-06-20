package com.example.app

import com.getcapacitor.JSObject

abstract class ScraperTask: IScraperTask {
  fun scrapData(html: String) {
    val json = JSObject()
    json.put("data", html)
    PluginCallSingleton.pluginCall.resolve(json)

    scrapers.peek()?.let { webView.loadUrl(it.url) }
  }

  fun getJavascript(): String
  {
    var javascript = "";
    if (scraperConfig.ids.isNotEmpty()) {
      javascript =
        "(function() { return (document.getElementById('${scraperConfig.ids[0]}').innerHTML); })();"
    } else if (scraperConfig.classes.isNotEmpty()) {
      javascript = "(function() { return (document.querySelector('.${
        scraperConfig.classes.joinToString(
          "."
        )
      }').innerHTML); })();"
    }
    return javascript
  }
}
