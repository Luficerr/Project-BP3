package com.pab.funsplash.ui

import android.os.Bundle
import android.os.Environment
import android.app.DownloadManager
import android.net.Uri
import android.transition.TransitionInflater
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.pab.funsplash.R
import com.pab.funsplash.data.api.RetrofitClient
import com.pab.funsplash.data.model.PhotoDetail
import com.pab.funsplash.data.local.AppDatabase
import com.pab.funsplash.data.repository.PhotoRepository
import com.pab.funsplash.data.repository.BookmarkRepository
import com.pab.funsplash.databinding.ActivityDetailPhotoBinding
import com.pab.funsplash.ui.viewmodel.DetailPhotoViewModel
import com.pab.funsplash.ui.viewmodel.DetailPhotoViewModelFactory
import com.pab.funsplash.utils.dp
import kotlin.math.abs

class DetailPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPhotoBinding
    private lateinit var viewModel: DetailPhotoViewModel
    private lateinit var currentPhoto: PhotoDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("DETAIL", "photo_id = ${intent.getStringExtra("photo_id")}")

        val photoId = intent.getStringExtra("photo_id")
        val photoUrl = intent.getStringExtra("photo_url")

        if (photoId.isNullOrEmpty()) {
            Log.e("DETAIL", "photo_id is null, finishing activity")
            finish()
            return
        }

        setupToolbar()
        setupAnimation(photoId)
        setupViewModel()
        setupListeners()

        observeData()
        viewModel.loadPhoto(photoId)
        viewModel.checkBookmark(photoId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.appBarLayout.addOnOffsetChangedListener { appBar, offset ->
            val progress = abs(offset).toFloat() / appBar.totalScrollRange
            binding.toolbar.alpha = progress
            binding.toolbar.navigationIcon?.alpha = (progress * 255).toInt()
        }
    }

    private fun setupAnimation(photoId: String) {
        window.sharedElementEnterTransition =
            TransitionInflater.from(this)
                .inflateTransition(android.R.transition.move)

        binding.ivPhotoDetail.transitionName = "photo_$photoId"
    }

    private fun setupViewModel() {

        val bookmarkDb = AppDatabase.getInstance(this)
        val bookmarkRepo = BookmarkRepository(bookmarkDb.bookmarkDao())

        viewModel = ViewModelProvider(
            this,
            DetailPhotoViewModelFactory(
                PhotoRepository(RetrofitClient.api),
                bookmarkRepo
            )
        )[DetailPhotoViewModel::class.java]
    }

    private fun setupListeners() {
        binding.btnBookmark.setOnClickListener {
            viewModel.toggleBookmark(currentPhoto)
        }
    }

    private fun observeData() {
        viewModel.photo.observe(this) { photo ->
            currentPhoto = photo
            bindPhoto(photo)
            viewModel.checkBookmark(photo.id)
        }

//        viewModel.isBookmarked.observe(this) { bookmarked ->
//            updateBookmarkIcon(bookmarked)
//            showBookmarkFeedback(bookmarked)
//        }

        viewModel.bookmarkEvent.observe(this) { isAdded ->
            showBookmarkFeedback(isAdded)
        }

        viewModel.isBookmarked.observe(this) { bookmarked ->
            updateBookmarkIcon(bookmarked)
        }
    }

    private fun bindPhoto(photo: PhotoDetail) {
        Glide.with(this)
            .load(photo.urls.full ?: photo.urls.regular)
            .into(binding.ivPhotoDetail)

        binding.ivPhotoDetail.contentDescription =
            photo.altDescription ?: "Photo by ${photo.user.name}"

        binding.tvAuthor.text = photo.user.name
        binding.tvLikes.text = "${photo.likes} likes"
        binding.tvDownloads.text = "${photo.downloads ?: 0} downloads"
        binding.tvDescription.text = photo.description ?: "No description"

        binding.btnDownload.setOnClickListener {
            val downloadUrl = photo.urls.full
                ?: photo.urls.regular
                ?: photo.urls.small
                ?: return@setOnClickListener

            downloadPhoto(
                url = downloadUrl,
                fileName = "funsplash_${photo.id}"
            )
        }
    }

    private fun updateBookmarkIcon(isBookmarked: Boolean) {
        binding.btnBookmark.setImageResource(
            if (isBookmarked) R.drawable.ic_bookmark
            else R.drawable.ic_bookmark_border
        )

        binding.btnBookmark.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(120)
            .withEndAction {
                binding.btnBookmark.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(120)
                    .start()
            }
            .start()
    }

    private fun showBookmarkFeedback(isAdded: Boolean) {
        Log.d("BOOKMARK", "Snackbar shown, added = $isAdded")

        val icon = if (isAdded)
            ContextCompat.getDrawable(this, R.drawable.ic_check_circle)
        else
            ContextCompat.getDrawable(this, R.drawable.ic_bookmark_remove)

        icon?.setBounds(
            0,
            0,
            icon.intrinsicWidth,
            icon.intrinsicHeight
        )

        val message = if (isAdded)
            "  Post added to your Bookmarks"
        else
            "  Post removed from your Bookmarks"

        val spannable = SpannableString(message)
        spannable.setSpan(
            ImageSpan(icon!!, ImageSpan.ALIGN_CENTER),
            0,
            1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        Snackbar.make(binding.root, spannable, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.appBarLayout)
            .show()
    }

    private fun downloadPhoto(
        url: String,
        fileName: String
    ) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading photo")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "Funsplash/$fileName.jpg"
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadManager.enqueue(request)

        Toast.makeText(
            this,
            "Download started",
            Toast.LENGTH_SHORT
        ).show()
    }
}