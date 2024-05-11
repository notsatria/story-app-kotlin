package com.notsatria.storyapp.ui.main.ui.addstory

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.notsatria.storyapp.R
import com.notsatria.storyapp.databinding.FragmentAddStoryBinding
import com.notsatria.storyapp.ui.main.MainActivity

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addStoryViewModel =
            ViewModelProvider(this).get(AddStoryViewModel::class.java)

        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else
            showToast(getString(R.string.image_not_available))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }
}