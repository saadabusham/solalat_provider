package com.raantech.solalat.provider.ui.media.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.raantech.solalat.provider.data.api.response.APIResource
import com.raantech.solalat.provider.data.repos.media.MediaRepo
import com.raantech.solalat.provider.ui.base.viewmodel.BaseViewModel
import okhttp3.MultipartBody

class MediaViewModel @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        val mediaRepo: MediaRepo
) : BaseViewModel() {


    fun getMedia(skip: Int) = liveData {
        emit(APIResource.loading())
        val response = mediaRepo.getMedia(skip)
        emit(response)
    }

    fun uploadMedia(media : MultipartBody.Part) = liveData {
        emit(APIResource.loading())
        val response = mediaRepo.uploadMedia(media)
        emit(response)
    }

    fun deleteMedia(mediaId: Int) = liveData {
        emit(APIResource.loading())
        val response = mediaRepo.deleteMedia(mediaId)
        emit(response)
    }
}