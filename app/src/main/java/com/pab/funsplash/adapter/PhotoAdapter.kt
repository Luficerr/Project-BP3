package com.pab.funsplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pab.funsplash.databinding.ItemPhotoBinding
import com.pab.funsplash.data.model.Photo

class PhotoAdapter(
    private val photos: List<Photo>,
    private val onClick: (Photo) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.binding.imgPhoto.setImageResource(photo.imageRes)
        holder.binding.tvUser.text = "by ${photo.username}"

        holder.itemView.setOnClickListener {
            onClick(photo)
        }
    }

    override fun getItemCount() = photos.size
}