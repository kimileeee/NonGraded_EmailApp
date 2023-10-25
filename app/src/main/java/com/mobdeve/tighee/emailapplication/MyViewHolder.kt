package com.mobdeve.tighee.emailapplication

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.tighee.emailapplication.databinding.ItemLayoutBinding

class MyViewHolder(private val viewBinding: ItemLayoutBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the email data to the views in the ViewHolder
    fun bindData(email: Email) {
        this.viewBinding.itemReceiver.text = email.receiver
        this.viewBinding.itemSubject.text = email.subject
        this.viewBinding.itemBody.text = removeNewLineCharacters(email.body)
    }

    // Helper function to format the text of the body expected in the ViewHolder
    private fun removeNewLineCharacters(input: String): String {
        return input.replace("\n", " ")
    }
}