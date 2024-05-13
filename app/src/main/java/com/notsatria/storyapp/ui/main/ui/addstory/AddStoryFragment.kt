package com.notsatria.storyapp.ui.main.ui.addstory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.databinding.FragmentAddStoryBinding
import com.notsatria.storyapp.ui.main.MainActivity
import com.notsatria.storyapp.utils.ViewModelFactory
import com.notsatria.storyapp.utils.getImageUri
import com.notsatria.storyapp.utils.reduceFileImage
import com.notsatria.storyapp.utils.uriToFile

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var addStoryViewModel: AddStoryViewModel
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()


        with(binding) {
            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCamera.setOnClickListener {
                startCamera()
            }

            buttonAdd.setOnClickListener {
                postStory()
            }

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        addStoryViewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
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

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }

    }

    private fun postStory() {
        Log.d(
            "AddStoryFragment",
            "ImageUri: $currentImageUri Description: ${binding.edAddDescription.text}"
        )
        if (binding.edAddDescription.text!!.isBlank() || currentImageUri == null) {
            showToast(getString(R.string.image_and_description_couldnt_be_empty))
            return
        }

        currentImageUri?.let { uri ->
            val description = binding.edAddDescription.text.toString()
            val imageFile = uriToFile(uri, requireContext())

            addStoryViewModel.postStory(description, imageFile.reduceFileImage())
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        MainActivity::class.java
                                    ).addFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    )
                                )
                            }

                            else -> {
                                showLoading(false)
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }
                    }
                }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showImage() {
        currentImageUri.let {
            binding.previewImageView.setImageURI(it)
        }
    }
}