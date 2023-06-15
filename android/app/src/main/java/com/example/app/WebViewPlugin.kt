package com.example.app

import android.content.Intent
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin



@CapacitorPlugin
class WebView : Plugin() {

  @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
  open fun show(call: PluginCall) {
    val url = call.getString("value")
    val myIntent = Intent(bridge.webView.context, WebViewActivity::class.java)
    myIntent.putExtra("url", url)
    bridge.webView.context.startActivity(myIntent)

    call.setKeepAlive(true)
    PluginCallSingleton.pluginCall = call
  }

}





