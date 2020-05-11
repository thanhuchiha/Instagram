package com.thanhuhiha.instagram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.thanhuhiha.instagram.R

open class Fragment: Fragment() {
    var image: String? = ""
    private val TAG = "A"
    lateinit var imageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment, container, false)
        image = arguments?.getString("img")

        //rootView.output_textview.text = inputText
        //rootView.image_view. = inputText
        imageView = rootView.findViewById(R.id.image_view)!!
        //imageView.setImageBitmap(BitmapFactory.decodeFile(image))

        Picasso.get().load(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error).fit()
            .into(imageView)

        Log.d(TAG, "aaaaa $image")
        return rootView
    }
}