package com.google.firebase.udacity.friendlychat.Message.view

import android.app.Activity
import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.udacity.friendlychat.BR
import com.google.firebase.udacity.friendlychat.R
import com.google.firebase.udacity.friendlychat.databinding.ItemMessageBinding
import com.google.firebase.udacity.friendlychat.Message.model.FriendlyMessage

class MessageAdapter(context: Context, resource: Int, objects: List<FriendlyMessage>) : ArrayAdapter<FriendlyMessage>(context, resource, objects) {

    lateinit var view: View

    var photoUrl = ""
    var text = ""
    var username = ""

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        initialize(parent)
        val message = getItem(position)
        photoUrl = message.photoUrl
        text = message.text
        username = message.username
        val itemMessageBinding: ItemMessageBinding = DataBindingUtil.bind(view)
        itemMessageBinding.setVariable(BR.message, this)
        itemMessageBinding.executePendingBindings()
            return view
        }

    private fun initialize(parent: ViewGroup){
            view = (context as Activity).layoutInflater.inflate(R.layout.item_message, parent, false)
    }

    companion object {
        @BindingAdapter("android:src")
        @JvmStatic fun setSrc(imageView: ImageView, url: String){
            if (url != "")
                Glide.with(imageView.context)
                        .load(url)
                        .into(imageView)
            else {
                imageView.visibility = View.GONE
            }
        }
    }


}
