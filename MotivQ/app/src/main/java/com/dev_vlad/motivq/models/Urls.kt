package com.dev_vlad.motivq.models

import kotlin.random.Random

object Urls {

    private val categories = arrayOf("management", "life", "funny", "love", "art" , "students")

    fun getRandomQuoteUrl() : String{
        val randomCategory = categories[Random.nextInt(categories.size)]
        return "http://quotes.rest/qod.json?category=$randomCategory"
    }

    fun getTodayQuoteUrl() : String{
       return "http://quotes.rest/qod.json?category=inspire"
    }
}

