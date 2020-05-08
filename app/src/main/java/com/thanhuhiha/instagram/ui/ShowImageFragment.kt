package com.thanhuhiha.instagram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.thanhuhiha.instagram.R

class ShowImageFragment : Fragment() {
    var image: String? = ""
    private lateinit var imageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_show_image, container, false)
        image = arguments?.getString("img")
        Log.d("a", "$image")
        return rootView
    }
}