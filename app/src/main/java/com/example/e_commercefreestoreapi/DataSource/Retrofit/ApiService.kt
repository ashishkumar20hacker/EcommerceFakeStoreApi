package com.example.e_commercefreestoreapi.DataSource.Retrofit

import com.example.e_commercefreestoreapi.DataSource.Model.LoginRequestModel
import com.example.e_commercefreestoreapi.DataSource.Model.LoginResponseModel
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModel
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModelItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    fun userLogin(@Body loginRequestModel: LoginRequestModel) : Call<LoginResponseModel>

    @GET("products")
    fun getAllProducts() : Call<ProductsResponseModel>

    @GET("products/{id}")
    fun getSingleProduct(@Path("id") productId: Int): Call<ProductsResponseModelItem>

    @GET("products")
    fun getSortedProducts(
        @Query("sort") sort: String
    ) : Call<ProductsResponseModel>

    @GET("products/category/{category}")
    fun getFilteredProducts(
        @Path("category") category: String
    ) : Call<ProductsResponseModel>

}