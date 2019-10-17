package com.dev_vlad.motivq.async_tasks
import android.os.AsyncTask
import android.util.Log
import java.net.InetAddress

class AsyncInternetChecker : AsyncTask<Void, Void, Boolean>() {

    interface AsyncResponse {
        fun checkingComplete(isConnected: Boolean)
    }

    var delegateResult: AsyncResponse? = null


    override fun doInBackground(vararg params: Void?): Boolean {
        return isInternetAvailable()
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        delegateResult!!.checkingComplete(result)
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddress = InetAddress.getByName("firebase.google.com")
            Log.d("fire-base ip address", ipAddress.toString())
            ipAddress.toString() != ""

        } catch (e: Exception) {
            false
        }

    }
}