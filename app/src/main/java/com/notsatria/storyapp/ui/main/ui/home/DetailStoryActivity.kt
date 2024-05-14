package com.notsatria.storyapp.ui.main.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.notsatria.storyapp.R
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.remote.response.Story
import com.notsatria.storyapp.databinding.ActivityDetailStoryBinding
import com.notsatria.storyapp.ui.auth.LoginActivity
import com.notsatria.storyapp.utils.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private var transitionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViewModel()

        val storyId = intent.getStringExtra(STORY_ID)
        transitionName = intent.getStringExtra(TRANSITION_NAME)

        detailStoryViewModel.getStoryDetail(storyId ?: "")
            .observe(this@DetailStoryActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            setupView(result.data.story!!)
                        }

                        is Result.Error -> {
                            showDialog(
                                this@DetailStoryActivity,
                                resources.getString(R.string.session_expired_title),
                                resources.getString(R.string.session_expired_message)
                            )
                        }

                        else -> {
                            showLoading(false)
                            showError()
                        }
                    }
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView(story: Story) {
        with(binding) {
            Log.d("DetaiStoryActivity", "setupView: $transitionName")
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_image)
                .into(ivDetailPhoto)

            ivDetailPhoto.transitionName = transitionName
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError() {
        binding.errorLayout.visibility = View.VISIBLE
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
                this@DetailStoryActivity,
                activity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@DetailStoryActivity)
        detailStoryViewModel = ViewModelProvider(this, factory)[DetailStoryViewModel::class.java]
    }

    companion object {
        const val STORY_ID = "story_id"
        const val TRANSITION_NAME = "transition_name"
    }

}