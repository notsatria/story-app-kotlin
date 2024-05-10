package com.notsatria.storyapp.ui.main.ui.addstory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.notsatria.storyapp.databinding.FragmentAddStoryBinding
import com.notsatria.storyapp.ui.main.MainActivity

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addStoryViewModel =
            ViewModelProvider(this).get(AddStoryViewModel::class.java)

        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as MainActivity).supportActionBar.apply {
            this?.title = "Add Story"
            this?.elevation = 0f
            this?.isHideOnContentScrollEnabled = true
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}