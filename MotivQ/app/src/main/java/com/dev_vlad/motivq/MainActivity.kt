package com.dev_vlad.motivq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.dev_vlad.motivq.async_tasks.AsyncFetchQuote
import com.dev_vlad.motivq.async_tasks.AsyncInternetChecker
import com.dev_vlad.motivq.models.Quotes
import com.dev_vlad.motivq.ui.InfoPopUp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , InfoPopUp.OperationFeedbackDialogListener{


    private var infoPopUp : InfoPopUp? = null
    private var todaysQuoteFetched  = false  //ensure that today's quote is fetched only once


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_act_toolbar)

    }

    private fun setQuoteOfTheDay() {
        //get today's quote
        val fetchTodayQuoteTask = AsyncFetchQuote(fetchRandom = false)
        fetchTodayQuoteTask.delegateResult = object : AsyncFetchQuote.AsyncResponse{
            override fun fetchingComplete(quote: Quotes?) {
                if(quote != null){
                    todaysQuoteFetched = true
                    the_quote_tv.text = quote.quote
                    val authorPrefixed = getString(R.string.author_prefix_txt)+ " " + quote.author
                    the_author_tv.text =  authorPrefixed
                    the_author_tv.visibility = View.VISIBLE
                }else{
                    Toast.makeText(this@MainActivity, R.string.err_fetching_quote, Toast.LENGTH_LONG).show()
                }
            }
        }
        fetchTodayQuoteTask.execute()
    }



    /********* ON VIEW ACTION LISTENERS ***************/

    fun onGetRandomQuoteBtnClicked(view: View) {
        //show progress bar
        progress_circular.visibility = View.VISIBLE
        view.visibility = View.INVISIBLE

        val fetchRandomQuoteTask = AsyncFetchQuote(fetchRandom = true)
        fetchRandomQuoteTask.delegateResult = object : AsyncFetchQuote.AsyncResponse{
            override fun fetchingComplete(quote: Quotes?) {
                if(quote != null){
                    val randomQuote = quote.quote + " " + getString(R.string.author_prefix_txt) + " " + quote.author
                    displayInfoAsPopUp(getString(R.string.random_quote_title), randomQuote,  isPositiveMsg = true, closeActOnDismiss = false)
                }else{
                    val errMsg = getString( R.string.err_fetching_quote)
                    displayInfoAsPopUp(getString(R.string.random_quote_title), errMsg,  isPositiveMsg = false, closeActOnDismiss = false)
                }

                progress_circular.visibility = View.INVISIBLE
                view.visibility = View.VISIBLE
            }
        }
        fetchRandomQuoteTask.execute()
    }








    /******** MENUS **************/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflate menu
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId  ==  R.id.show_credits_item-> {
                val popUpTitle = getString(R.string.credits_txt)
                val credits = getString(R.string.app_credits)
                displayInfoAsPopUp(popUpTitle, message = credits, isPositiveMsg = true, closeActOnDismiss = false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    /************** LIFE CYCLE METHODS ********************/


    override fun onResume() {
        super.onResume()
        checkForInternetConnection()
    }


    private fun checkForInternetConnection() {
        val asyncInternetChecker  = AsyncInternetChecker()
        asyncInternetChecker.delegateResult = object : AsyncInternetChecker.AsyncResponse{
            override fun checkingComplete(isConnected: Boolean) {
                if(!isConnected)
                    displayInfoAsPopUp(getString(R.string.internet_connection_title) , getString(R.string.internet_connection_err), isPositiveMsg = false , closeActOnDismiss = true)

                else if(!todaysQuoteFetched){
                    setQuoteOfTheDay()
                }
            }
        }
        asyncInternetChecker.execute()
    }



    /************* POP UP MESSAGES ***************************/

    private fun displayInfoAsPopUp(popUpTitle : String, message : String, isPositiveMsg : Boolean, closeActOnDismiss : Boolean){
        infoPopUp = InfoPopUp(popUpTitle, message, isPositiveMsg, closeActOnDismiss)
        infoPopUp!!.show(supportFragmentManager, "tag_name_motiv-q_info")
    }

    override fun onInfoPopUpClosed(closeAct : Boolean) {
        infoPopUp!!.dismiss()
        if(closeAct)
            finish()
    }



}
