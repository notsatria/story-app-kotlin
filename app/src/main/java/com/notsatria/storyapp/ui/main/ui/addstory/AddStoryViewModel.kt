package com.notsatria.storyapp.ui.main.ui.addstory

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddStoryViewModel : ViewModel() {

    private val _currentImageUri = MutableLiveData<Uri>().apply {
        value = null
    }
    val currentImageUri: LiveData<Uri> = _currentImageUri

    fun setImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }
}