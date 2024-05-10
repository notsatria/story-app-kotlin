package com.notsatria.storyapp.ui.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.notsatria.storyapp.databinding.FragmentDetailStoryBinding
import com.notsatria.storyapp.ui.main.MainActivity

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).supportActionBar.apply {
            this?.title = "Story Detail"
            this?.elevation = 0f
            this?.isHideOnContentScrollEnabled = true
            this?.setHomeButtonEnabled(true)
        }

        return root
    }

}