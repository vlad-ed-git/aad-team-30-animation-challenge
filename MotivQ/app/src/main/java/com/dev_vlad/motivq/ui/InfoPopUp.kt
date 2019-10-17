package com.dev_vlad.motivq.ui

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dev_vlad.motivq.R

class InfoPopUp (private val popUpTitle: String, private val message : String, private val isMessagePositive : Boolean, private val closeActOnDismiss: Boolean) : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: OperationFeedbackDialogListener

    interface OperationFeedbackDialogListener {
        fun onInfoPopUpClosed(closeAct : Boolean)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val view: View = inflater.inflate(R.layout.info_pop_up_layout, null)
            builder.setView(view)
            val dismissBtn = view.findViewById<Button>(R.id.dismiss_btn)
            val popUpTitleTv = view.findViewById<TextView>(R.id.pop_up_title_tv)
            val positiveMsgTv = view.findViewById<TextView>(R.id.positive_msg_tv)
            val negativeMsgTv = view.findViewById<TextView>(R.id.negative_msg_tv)


            popUpTitleTv.text =  popUpTitle
            if(isMessagePositive) {
                negativeMsgTv.visibility = View.GONE
                positiveMsgTv.visibility = View.VISIBLE
                positiveMsgTv.text = message
            }else {
                positiveMsgTv.visibility = View.GONE
                negativeMsgTv.visibility = View.VISIBLE
                negativeMsgTv.text = message
            }

            dismissBtn.setOnClickListener{listener.onInfoPopUpClosed(closeActOnDismiss)}


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as OperationFeedbackDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement OperationFeedbackDialogListener")
            )
        }

    }

}