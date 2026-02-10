package com.pab.funsplash.ui.fragments

import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pab.funsplash.databinding.FragmentBookmarkBinding
import com.pab.funsplash.ui.DetailPhotoActivity
import com.pab.funsplash.ui.adapter.BookmarkAdapter
import com.pab.funsplash.ui.viewmodel.BookmarkViewModel
import com.pab.funsplash.ui.viewmodel.BookmarkViewModelFactory
import com.pab.funsplash.data.local.AppDatabase
import com.pab.funsplash.data.repository.BookmarkRepository
import com.google.android.material.snackbar.Snackbar

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BookmarkViewModel
    private lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookmarkAdapter { bookmark ->
            val intent = Intent(requireContext(), DetailPhotoActivity::class.java)
            intent.putExtra("photo_id", bookmark.photoId)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BookmarkFragment.adapter
        }

        val dao = AppDatabase.getInstance(requireContext()).bookmarkDao()
        val repository = BookmarkRepository(dao)

        viewModel = ViewModelProvider(
            this,
            BookmarkViewModelFactory(repository)
        )[BookmarkViewModel::class.java]

        viewModel.bookmarks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyView.visibility =
                if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedItem = adapter.currentList[position]

                    // hapus dari DB
                    viewModel.deleteBookmark(deletedItem)

                    // snackbar undo
                    Snackbar.make(
                        binding.root,
                        "Bookmark removed",
                        Snackbar.LENGTH_LONG
                    ).setAction("UNDO") {
                        viewModel.restoreBookmark(deletedItem)
                    }.show()
                }
            }
        )

        // attach ke RecyclerView
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
