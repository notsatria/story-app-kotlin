package com.notsatria.storyapp.ui.main.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.databinding.FragmentHomeBinding
import com.notsatria.storyapp.ui.adapter.StoryItemAdapter
import com.notsatria.storyapp.ui.auth.LoginActivity
import com.notsatria.storyapp.ui.main.MainActivity
import com.notsatria.storyapp.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var storySection: Section

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initViewModel()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).supportActionBar.apply {
            this?.title = "Stories"
            this?.elevation = 0f
            this?.isHideOnContentScrollEnabled = true
        }

        homeViewModel.getToken().observe(viewLifecycleOwner) { token ->
            homeViewModel.fetchAllStories(token).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val storyItemAdapterList = result.data.listStory.map {
                                StoryItemAdapter(it)
                            }
                            initRecyclerView(storyItemAdapterList)
                        }

                        is Result.Error -> showDialog(
                            requireContext(),
                            getString(R.string.session_expired_title),
                            getString(R.string.session_expired_message)
                        )

                        else -> showLoading(false)
                    }
                }
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
                homeViewModel.clearAllSession()
            }
            .setOnDismissListener {
                destroyActivity(LoginActivity())
                homeViewModel.clearAllSession()
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
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun initRecyclerView(items: List<StoryItemAdapter>) {
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                storySection = Section(items)
                add(storySection)
            }
        }
    }
}