package com.pab.funsplash.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pab.funsplash.R
import com.pab.funsplash.databinding.ActivityMainBinding
import com.pab.funsplash.ui.fragments.BookmarkFragment
import com.pab.funsplash.ui.fragments.HomeFragment
import com.pab.funsplash.ui.fragments.ProfileFragment
import com.pab.funsplash.ui.fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
//                R.id.nav_search -> loadFragment(SearchFragment())
                R.id.nav_bookmark -> loadFragment(BookmarkFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}