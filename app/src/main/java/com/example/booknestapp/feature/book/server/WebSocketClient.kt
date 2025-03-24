package com.example.booknestapp.feature.book.server

import android.util.Log
import okhttp3.*

class WebSocketClient() {

    private val client = OkHttpClient.Builder()
        .pingInterval(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val request = Request.Builder()
        .url("ws://10.0.2.2:2528") // WebSocket server URL
        .build()

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
            Log.d("WebSocket", "WebSocket opened!")
            webSocket.send("Hello from Android client!") // Example message
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("WebSocket", "Received message: $text")
            // Handle real-time updates (optional)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
            Log.e("WebSocket", "Error: ${t.message}")
        }
    }

    fun startWebSocket() {
        client.newWebSocket(request, listener)
    }

    fun closeWebSocket() {
        client.dispatcher.executorService.shutdown()
    }

}
