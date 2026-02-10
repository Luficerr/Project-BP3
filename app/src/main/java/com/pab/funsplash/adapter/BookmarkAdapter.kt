package com.pab.funsplash.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pab.funsplash.databinding.ItemBookmarkBinding
import com.pab.funsplash.data.model.BookmarkEntity

class BookmarkAdapter(
    private val onClick: (BookmarkEntity) -> Unit
) : ListAdapter<BookmarkEntity, BookmarkAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(
        private val binding: ItemBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookmarkEntity) {
            binding.tvAuthor.text = item.author
            binding.tvLikes.text = "${item.likes} likes"
            binding.tvDescription.text = item.description ?: ""

            Glide.with(binding.root)
                .load(item.imageUrl)
                .into(binding.ivPhoto)

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<BookmarkEntity>() {
            override fun areItemsTheSame(
                oldItem: BookmarkEntity,
                newItem: BookmarkEntity
            ) = oldItem.photoId == newItem.photoId

            override fun areContentsTheSame(
                oldItem: BookmarkEntity,
                newItem: BookmarkEntity
            ) = oldItem == newItem
        }
    }
}
