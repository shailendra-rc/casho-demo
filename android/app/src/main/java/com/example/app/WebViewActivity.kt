package com.example.app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityWebViewBinding
import com.getcapacitor.JSObject


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebViewActivity : AppCompatActivity() {

  private lateinit var binding: ActivityWebViewBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityWebViewBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    // Find the WebView by its unique ID

    // Find the WebView by its unique ID
    val webView: WebView = binding.web

    val url = intent.extras?.get("url").toString()

    // this will enable the javascript.
    webView.getSettings().setJavaScriptEnabled(true)


    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

//      val amazonCookies = CookieManager.getInstance().getCookie("https://www.amazon.in")
//      val netflixCookies = CookieManager.getInstance().getCookie("https://www.netflix.com")
//      CookieManager.getInstance().removeAllCookies {


//        CookieManager.getInstance().setCookie("https://www.amazon.in", amazonCookies);
//        CookieManager.getInstance().setCookie("https://www.netflix.com", netflixCookies);

    // loading url in the WebView.
    webView.loadUrl(url)

    val builder = AlertDialog.Builder(this)

    webView.webViewClient = object : WebViewClient() {

      override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
      }
      override fun onPageFinished(view: WebView?, url: String?) {
        if (url?.indexOf("amazon.in")!! > 0) {
//          builder.setMessage(CookieManager.getInstance().getCookie("https://www.amazon.in"))
//            .setTitle("Amazon Cookies")
          webView.evaluateJavascript(
            "(function() { return (document.getElementById('ordersContainer').innerHTML); })();",
            ValueCallback<String?> { html ->
//              builder.setMessage(html)
//                .setTitle("test")
//              val dialog2 = builder.create()
//              dialog2.show()
              val json = JSObject()
              json.put("data", html!!)
              PluginCallSingleton.pluginCall.resolve(json)
            })
        }
        else if (url?.indexOf("netflix.com")!! > 0) {
//          builder.setMessage(CookieManager.getInstance().getCookie("https://www.netflix.com"))
//            .setTitle("Netflix Cookies")
          webView.evaluateJavascript(
            "(function() { return (document.querySelector('.structural.retable.stdHeight').innerHTML); })();",
            ValueCallback<String?> { html ->
//              Log.d("HTML", html!!)
              val json = JSObject()
              json.put("data", html!!)
              PluginCallSingleton.pluginCall.resolve(json)
            })
        }
        else if (url?.indexOf("riders.uber.com")!! > 0) {
//          builder.setMessage(CookieManager.getInstance().getCookie("https://uber.com"))
//            .setTitle("Uber Cookies")

          Thread(UberWebViewAsyncTask(this@WebViewActivity, webView)).start()
        }
      }
    }
  }
}





