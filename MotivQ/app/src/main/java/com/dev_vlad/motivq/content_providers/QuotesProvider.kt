package com.dev_vlad.motivq.content_providers

import android.util.Log
import com.dev_vlad.motivq.models.Quotes
import com.dev_vlad.motivq.models.Urls
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class QuotesProvider {


    fun getRandom() : Quotes?{
        return try {
            val tQUrl = Urls.getRandomQuoteUrl()
            val jsonStr = URL(tQUrl).readText()
            parseJsonResponse(jsonStr)

        }catch (exc : Exception){
            Log.d("error getting url", exc.message, exc.cause)
            null
        }
    }


    fun getTodaysQuote() : Quotes?{
        return try{
            val tQUrl = Urls.getTodayQuoteUrl()
            val jsonStr = URL(tQUrl).readText()
            parseJsonResponse(jsonStr)

        }catch (exc : Exception){
            Log.d("error getting url", exc.message, exc.cause)
            null
        }
    }

    private fun parseJsonResponse(jsonStr : String) : Quotes?{
        return try {
            val theQuote = Quotes()
            val quotesJsonArray =
                JSONObject(jsonStr).getJSONObject("contents").getJSONArray("quotes")
            theQuote.quote = quotesJsonArray.getJSONObject(0).getString("quote")
            theQuote.author = quotesJsonArray.getJSONObject(0).getString("author")
            theQuote
        }catch (exc : Exception){
            Log.d("error parsing json", exc.message, exc.cause)
            null
        }
    }


}