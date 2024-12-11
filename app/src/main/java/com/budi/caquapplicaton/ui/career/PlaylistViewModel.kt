package com.budi.caquapplicaton.ui.career


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.PlaylistItem
import com.budi.caquapplicaton.room.AuthRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistApiService: AuthService,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _playlist = MutableLiveData<PlaylistItem>()
    val playlist: LiveData<PlaylistItem> get() = _playlist

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchPlaylist(name: String, token: String) {
        viewModelScope.launch {
            try {
                val response = playlistApiService.getPlaylist(name, "Bearer $token")
                if (response.isSuccessful) {
                    _playlist.postValue(response.body())
                } else {
                    _errorMessage.postValue("Failed to fetch playlist: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }
}
