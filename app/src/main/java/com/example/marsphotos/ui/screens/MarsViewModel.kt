/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.network.MarsPhoto
import java.io.IOException

// Define the MarsUiState with a Success state that holds a list of MarsPhoto objects
sealed interface MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState // Updated to hold List<MarsPhoto>
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel : ViewModel() {

    // Initialize marsUiState with Loading state
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    // Call the function to fetch photos when the ViewModel is initialized
    init {
        getMarsPhotos()
    }

    // Function to fetch Mars photos from the API
    private fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = try {
                // Fetch the photos from the API
                val listResult = MarsApi.retrofitService.getPhotos()
                // Update the UI state with the list of MarsPhoto objects
                MarsUiState.Success(listResult)
            } catch (e: IOException) {
                // Update the UI state with Error in case of an exception
                MarsUiState.Error
            }
        }
    }
}
