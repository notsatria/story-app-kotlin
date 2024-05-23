package com.notsatria.storyapp.ui.home

import android.app.ActivityOptions
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
import com.notsatria.storyapp.databinding.FragmentHomeBinding
import com.notsatria.storyapp.ui.adapter.LoadingStateAdapter
import com.notsatria.storyapp.ui.adapter.StoryItemAdapter
import com.notsatria.storyapp.ui.auth.LoginActivity
import com.notsatria.storyapp.utils.ViewModelFactory
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

        binding.fabMap.setOnClickListener {
            startActivity(Intent(requireActivity(), MapsActivity::class.java))
        }

        getStories()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getStories()
    }

    private fun getStories() {
        val adapter = StoryItemAdapter(onItemClick = { storyId, transitionName ->
            onStoryItemClick(storyId, transitionName)
        })
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.stories.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun onStoryItemClick(storyId: String, transitionName: String) {
        val intent = Intent(requireActivity(), DetailStoryActivity::class.java).apply {
            putExtra(
                DetailStoryActivity.STORY_ID,
                storyId
            )
            putExtra(
                DetailStoryActivity.TRANSITION_NAME,
                transitionName
            )
        }
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            binding.rvStory,
            transitionName
        )
        startActivity(
            intent,
            options.toBundle()
        )
        Log.d("HomeFragment", "Story ID: $storyId, Transition Name: $transitionName")
    }
}