package com.example.app

import android.app.Activity
import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.getcapacitor.JSObject


internal class UberWebViewAsyncTask(var context: Activity, var webView: WebView) : Runnable {
  var hasRides = false

  override fun run() {
    while (!hasRides) {
      this.context.runOnUiThread() {
        this.webView.evaluateJavascript(
          "(function() { return (document.querySelector('._css-gemfqT').innerHTML) })();"
        ) { html ->
          if (html.toString().isNotEmpty() && html.contains("Past Trips")) {
//            Log.d("UBER WEBVIEW INSIDE", html)

            var json = JSObject()
            json.put("data", html)
            PluginCallSingleton.pluginCall.resolve(json)
            this.hasRides = true
          }
        }
      }

      Thread.sleep(3000)
    }
  }
}
