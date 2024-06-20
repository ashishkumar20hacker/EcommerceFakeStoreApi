package com.example.e_commercefreestoreapi.DataSource.Model

data class ProductsResponseModelItem(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)