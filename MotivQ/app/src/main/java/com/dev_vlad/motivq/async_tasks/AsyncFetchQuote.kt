package com.dev_vlad.motivq.async_tasks

import android.os.AsyncTask
import com.dev_vlad.motivq.content_providers.QuotesProvider
import com.dev_vlad.motivq.models.Quotes

class AsyncFetchQuote(fetchRandom: Boolean) : AsyncTask<Void, Void, Quotes?>() {

    private var fetchRandomQuote : Boolean = fetchRandom

    interface AsyncResponse {
        fun fetchingComplete(quote: Quotes?)
    }

    var delegateResult: AsyncResponse? = null


    override fun doInBackground(vararg params: Void): Quotes?{

        val quotesProvider = QuotesProvider()

        return if(fetchRandomQuote)
            quotesProvider.getRandom()
        else
            quotesProvider.getTodaysQuote()
    }

    override fun onPostExecute(quote: Quotes?) {
        super.onPostExecute(quote)
        delegateResult!!.fetchingComplete(quote)
    }


}