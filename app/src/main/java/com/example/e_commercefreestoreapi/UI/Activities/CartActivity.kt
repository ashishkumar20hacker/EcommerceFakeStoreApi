package com.example.e_commercefreestoreapi.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModelItem
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.UI.Adapter.ProductsAdapter
import com.example.e_commercefreestoreapi.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var preferences: SharePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SharePreferences(this)

        setRecyclerView()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@CartActivity, DashboardActivity::class.java))
                overridePendingTransition(0,0)
            }
        })

        binding.backbt.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setRecyclerView() {
        val list = preferences.getFromCartDataList()
        if (list.isNotEmpty()) {
            val adapter = ProductsAdapter(
                this,
                preferences,
                list,
                object : ProductsAdapter.ProductListener {
                    override fun addItemToCart(product: ProductsResponseModelItem) {
                    }

                    override fun removeItemFromCart(product: ProductsResponseModelItem) {
                        preferences.removeFromCart(product)
                        setRecyclerView()
                    }

                    override fun viewProduct(id: Int) {
                        startActivity(Intent(this@CartActivity, ProductDetailsActivity::class.java).putExtra("productId", id))
                    }
                })
            binding.productsRv.adapter = adapter
            binding.empty.visibility = View.GONE
            binding.productsRv.visibility = View.VISIBLE
        } else {
            binding.productsRv.visibility = View.GONE
            binding.empty.visibility = View.VISIBLE
        }
    }
}