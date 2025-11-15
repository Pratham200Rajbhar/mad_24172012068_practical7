package com.example.practical7

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

object HttpRequest {
    private const val TAG = "HttpRequest"

    fun makeServiceCall(reqUrl: String?, token: String? = null): String? {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
                conn.setRequestProperty("Content-Type", "application/json")
            }
            conn.requestMethod = "GET"
            conn.connectTimeout = 15000
            conn.readTimeout = 15000

            val input = BufferedInputStream(conn.inputStream)
            response = convertStreamToString(input)
            input.close()
            conn.disconnect()
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException: " + e.message)
        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: " + e.message)
        } catch (e: java.io.IOException) {
            Log.e(TAG, "IOException: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response
    }

    private fun convertStreamToString(input: java.io.InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine() ?: break
            sb.append(line)
        }
        return sb.toString()
    }
}
