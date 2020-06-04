package com.thanhuhiha.instagram.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.thanhuhiha.instagram.R


class ImagesAdapter() :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    lateinit var images: List<String>

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Onclick here
        val photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val photoView = inflater.inflate(R.layout.item_image, parent, false)
        return MyViewHolder(photoView)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photo = images[position]
        val imageView = holder.photoImageView
        Picasso.get().load(photo)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .fit().into(imageView)
    }

    fun updateImages(newImages: List<String>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(images, newImages) { it })
        this.images = newImages
        diffResult.dispatchUpdatesTo(this)
    }
}