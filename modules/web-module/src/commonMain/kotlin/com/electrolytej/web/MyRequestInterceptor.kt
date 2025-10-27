package com.electrolytej.web

import co.touchlab.kermit.Logger
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.WebViewNavigator

class MyRequestInterceptor : RequestInterceptor {
    companion object {
        val TAG = MyRequestInterceptor::class.simpleName
    }

    override fun onInterceptUrlRequest(
        request: WebRequest,
        navigator: WebViewNavigator,
    ): WebRequestInterceptResult {
        Logger.d {"onInterceptUrlRequest: $request"}
        return if (request.url.contains("kotlin")) {
            WebRequestInterceptResult.Modify(WebRequest(url = "https://kotlinlang.org/docs/multiplatform.html", headers = mutableMapOf("info" to "test"),),)
        } else {
            WebRequestInterceptResult.Allow
        }
    }
}