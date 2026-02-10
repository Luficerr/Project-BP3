package com.pab.funsplash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pab.funsplash.R
import com.pab.funsplash.data.model.UnsplashPhoto
import com.pab.funsplash.databinding.ItemPhotoBinding
import com.pab.funsplash.databinding.FooterLoadStateBinding

class UnsplashPhotoAdapter(
    private val onClick: (UnsplashPhoto) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_PHOTO = 1
        private const val VIEW_FOOTER = 2
    }

    private val photos = mutableListOf<UnsplashPhoto>()
    var showFooter = false
    var onRetry: (() -> Unit)? = null

    // ---------------- DATA ----------------

    fun setData(newPhotos: List<UnsplashPhoto>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    fun appendData(newPhotos: List<UnsplashPhoto>) {
        val start = photos.size
        photos.addAll(newPhotos)
        notifyItemRangeInserted(start, newPhotos.size)
    }

    fun showLoadingFooter(show: Boolean) {
        if (showFooter == show) return
        showFooter = show
        if (show) notifyItemInserted(itemCount)
        else notifyItemRemoved(itemCount)
    }

    // ---------------- ADAPTER ----------------

    override fun getItemCount(): Int =
        photos.size + if (showFooter) 1 else 0

    override fun getItemViewType(position: Int): Int =
        if (showFooter && position == photos.size) VIEW_FOOTER
        else VIEW_PHOTO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_PHOTO -> {
                val binding = ItemPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PhotoViewHolder(binding)
            }
            VIEW_FOOTER -> {
                val binding = FooterLoadStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FooterViewHolder(binding)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> holder.bind(photos[position])
            is FooterViewHolder -> holder.bind()
        }
    }

    // ---------------- VIEW HOLDERS ----------------

    inner class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: UnsplashPhoto) {
            Glide.with(binding.root)
                .load(photo.urls.small)
                .centerCrop()
                .into(binding.imgPhoto)

            binding.tvUser.text = photo.user.username
            binding.root.setOnClickListener { onClick(photo) }
        }
    }

    inner class FooterViewHolder(
        private val binding: FooterLoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRetry.visibility = View.GONE

            binding.btnRetry.setOnClickListener {
                onRetry?.invoke()
            }
        }
    }
}