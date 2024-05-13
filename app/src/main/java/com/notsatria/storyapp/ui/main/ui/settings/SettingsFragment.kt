package com.notsatria.storyapp.ui.main.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.notsatria.storyapp.databinding.FragmentSettingsBinding
import com.notsatria.storyapp.ui.auth.LoginActivity
import com.notsatria.storyapp.utils.ViewModelFactory

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var settingsStoryViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()

        with(binding) {
            btnLogout.setOnClickListener {
                logout()
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
        settingsStoryViewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    private fun logout() {
        settingsStoryViewModel.logout().also {
            startActivity(
                Intent(requireActivity(), LoginActivity::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )
        }
    }
}