package com.pab.funsplash.ui.fragments

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pab.funsplash.databinding.FragmentHomeBinding
import com.pab.funsplash.ui.DetailPhotoActivity
import com.pab.funsplash.adapter.UnsplashPhotoAdapter
import com.pab.funsplash.adapter.LoadingFooterAdapter
import com.pab.funsplash.data.repository.PhotoRepository
import com.pab.funsplash.data.api.RetrofitClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val repository by lazy {
        PhotoRepository(RetrofitClient.api)
    }
    private lateinit var photoAdapter: UnsplashPhotoAdapter

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ===== ADAPTER =====
        photoAdapter = UnsplashPhotoAdapter { photo ->
            val intent = Intent(requireContext(), DetailPhotoActivity::class.java)
            intent.putExtra("photo_id", photo.id)
            intent.putExtra("photo_url", photo.urls.regular)
            startActivity(intent)
        }

        photoAdapter.onRetry = {
            loadNextPage()
        }

        binding.rvPhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = photoAdapter
        }

        // LOAD AWAL
        loadInitial()

        // SWIPE REFRESH
        binding.swipeRefresh.setOnRefreshListener {
            refresh()
        }

        // INFINITE SCROLL
        binding.rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val lm = rv.layoutManager as GridLayoutManager
                val total = lm.itemCount
                val lastVisible = lm.findLastVisibleItemPosition()

                if (!isLoading && lastVisible >= total - 3 && dy > 0) {
                    loadNextPage()
                }
            }
        })
    }

    // ================= LOADERS =================

    private fun loadInitial() {
        lifecycleScope.launch {
            try {
                val photos = repository.getPhotos(forceRefresh = false)
                photoAdapter.setData(photos)
            } catch (e: Exception) {
                Log.e("HOME", "Initial load error", e)
            }
        }
    }

    private fun refresh() {
        lifecycleScope.launch {
            try {
                val photos = repository.getPhotos(forceRefresh = true)
                photoAdapter.setData(photos)
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun loadNextPage() {
        if (isLoading) return
        isLoading = true

        photoAdapter.showLoadingFooter(true)

        lifecycleScope.launch {
            try {
                val newPhotos = repository.getPhotos()
                photoAdapter.appendData(newPhotos)
                photoAdapter.showLoadingFooter(false)
            } catch (e: Exception) {
                photoAdapter.showLoadingFooter(true) // tampilkan retry
            } finally {
                isLoading = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}