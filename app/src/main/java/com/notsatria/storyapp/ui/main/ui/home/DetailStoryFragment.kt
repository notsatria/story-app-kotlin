package com.notsatria.storyapp.ui.main.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.storyapp.databinding.FragmentDetailStoryBinding
import com.notsatria.storyapp.ui.auth.LoginActivity
import com.notsatria.storyapp.ui.main.MainActivity
import com.notsatria.storyapp.utils.ViewModelFactory

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()

        homeViewModel.getStoryId().observe(viewLifecycleOwner) { storyId ->
            detailStoryViewModel.getStoryDetail(storyId)
                .observe(viewLifecycleOwner) { result ->
                    Log.d("DetailStoryFragment", "Result: $result")
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Logout") { _, _ ->
                destroyActivity(LoginActivity())
                detailStoryViewModel.clearAllSession()
            }
            .setOnDismissListener {
                destroyActivity(LoginActivity())
                detailStoryViewModel.clearAllSession()
            }
            .show()
    }

    private fun destroyActivity(activity: AppCompatActivity) {
        startActivity(
            Intent(
                requireActivity(),
                activity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        detailStoryViewModel = ViewModelProvider(this, factory)[DetailStoryViewModel::class.java]
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }


}