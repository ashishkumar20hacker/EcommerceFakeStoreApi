package com.example.e_commercefreestoreapi.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModel
import com.example.e_commercefreestoreapi.DataSource.Retrofit.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class DashboardViewModel : ViewModel() {

    private val TAG = "DashboardViewModel"

    fun getAllProducts(onResponse:(productList: ProductsResponseModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiClient.apiService.getAllProducts().awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                onResponse(response.body()!!)
            } else {
                Log.e(TAG, "Failed to Fetch Products!")
                onResponse(null)
            }
        }
    }

    fun getSortedProductList(type : String, onResponse:(productList: ProductsResponseModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiClient.apiService.getSortedProducts(type).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                onResponse(response.body()!!)
            } else {
                Log.e(TAG, "Failed to Fetch Products!")
                onResponse(null)
            }
        }
    }

    fun getFilteredProductList(type : String, onResponse:(productList: ProductsResponseModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiClient.apiService.getFilteredProducts(type).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                onResponse(response.body()!!)
            } else {
                Log.e(TAG, "Failed to Fetch Products!")
                onResponse(null)
            }
        }
    }

}