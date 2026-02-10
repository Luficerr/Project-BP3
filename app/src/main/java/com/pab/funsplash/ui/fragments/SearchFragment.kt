package com.pab.funsplash.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.Gravity
import androidx.fragment.app.Fragment
import com.pab.funsplash.R

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return TextView(requireContext()).apply {
            text = "Search"
            gravity = Gravity.CENTER
        }
    }
}