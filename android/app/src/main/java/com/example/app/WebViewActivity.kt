package com.example.app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityWebViewBinding
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.LinkedList
import java.util.Queue


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebViewActivity : AppCompatActivity() {
  private lateinit var scrapers: Queue<ScraperConfig>

  private lateinit var binding: ActivityWebViewBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityWebViewBinding.inflate(layoutInflater)
    setContentView(binding.root)
    val scrapers = LinkedList<ScraperConfig>()
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    // Find the WebView by its unique ID
    val webView: WebView = binding.web

    val scrapersStr = intent.extras?.getString("scrapers")
    val scrapersArray = JSONArray(scrapersStr)

    for (i in 0 until scrapersArray.length()) {
      val jsonObject: JSONObject = scrapersArray.getJSONObject(i)
      val scraperConfig = ScraperConfig()

      scraperConfig.url = jsonObject.getString("url")

      val classesArray = jsonObject.getJSONArray("classes")
      for (j in 0 until classesArray.length()) {
        scraperConfig.classes += classesArray.getString(j)
      }

      val idsArray = jsonObject.getJSONArray("ids")
      for (j in 0 until idsArray.length()) {
        scraperConfig.ids += idsArray.getString(j)
      }
      scraperConfig.isAsync = jsonObject.getBoolean("isAsync")
      scraperConfig.postLoginURL = jsonObject.getString("postLoginURL")
      scrapers.offer(scraperConfig)
    }
    // this will enable the javascript.
    webView.getSettings().setJavaScriptEnabled(true)


    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
    // loading url in the WebView.
    scrapers.peek()?.let { webView.loadUrl(it.url) }
    webView.webViewClient = object : WebViewClient() {

      override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
      }

      override fun onPageFinished(view: WebView?, url: String?) {
        val postLoginURL = scrapers.peek()?.postLoginURL
        val currentURL = scrapers.peek()?.url

        if ((url != null && !postLoginURL.isNullOrEmpty() && url.contains(postLoginURL))
          || ( url != null && currentURL !=null && url.contains(currentURL))) {

            scrapers.poll().let {
              if (it != null) {
                val type = if (it.isAsync) ScraperTaskType.UBER else ScraperTaskType.SYNC
                val scraperTask =
                  ScraperTaskFactory.getScraperTask(type, webView, it, scrapers, this@WebViewActivity)
                scraperTask?.runTask()
              }
            }
        }
      }
    }
  }
}
