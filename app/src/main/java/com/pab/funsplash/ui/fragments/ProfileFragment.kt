package com.pab.funsplash.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.pab.funsplash.R
import com.pab.funsplash.data.session.SessionManager
import com.pab.funsplash.ui.auth.SignInActivity
import com.pab.funsplash.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val pickImageLauncher =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                session.setProfileImage(it.toString())
                binding.ivProfile.setImageURI(it)
            }
        }
    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        session = SessionManager(requireContext())

        setupProfile()
        setupActions()

        return binding.root
    }

    private fun setupProfile() {
        binding.tvName.text = session.getName()
        binding.tvEmail.text = session.getEmail()

        val imageUri = session.getProfileImage()
        if (imageUri != null) {
            binding.ivProfile.setImageURI(Uri.parse(imageUri))
        } else {
            binding.ivProfile.setImageResource(R.drawable.user_avatar_placeholder)
        }

        binding.ivProfile.setOnClickListener {
            openImagePicker()
        }

        binding.ivEdit.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        pickImageLauncher.launch(arrayOf("image/*"))
    }

    private fun setupActions() {
        binding.btnLogout.setOnClickListener {
            session.logout()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
