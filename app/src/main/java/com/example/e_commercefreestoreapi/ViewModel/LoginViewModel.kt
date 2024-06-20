package com.example.e_commercefreestoreapi.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commercefreestoreapi.DataSource.Retrofit.ApiClient
import com.example.e_commercefreestoreapi.DataSource.Model.LoginRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class LoginViewModel : ViewModel() {

    private val TAG = "LoginViewModel"

    fun loginUser(userName: String, password: String, onResponse:(token: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val loginRequestModel = LoginRequestModel(userName, password)
            val response = ApiClient.apiService.userLogin(loginRequestModel).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                onResponse(response.body()!!.token)
            } else {
                Log.e(TAG, "Failed to Login!")
                onResponse("")
            }
        }
    }

}