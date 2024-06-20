package com.example.e_commercefreestoreapi.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.e_commercefreestoreapi.DataSource.Retrofit.ApiClient
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.R
import com.example.e_commercefreestoreapi.databinding.ActivityProductDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ProductDetailsActivity : AppCompatActivity() {

    private val TAG = "ProductDetailsActivity"
    lateinit var binding: ActivityProductDetailsBinding
    lateinit var preferences: SharePreferences
    var productId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SharePreferences(this)

        productId = intent.getIntExtra("productId", 1)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@ProductDetailsActivity, DashboardActivity::class.java))
                overridePendingTransition(0,0)
            }
        })

        binding.backbt.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        fetchAndShowDetails()

    }

    private fun fetchAndShowDetails() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiClient.apiService.getSingleProduct(productId).awaitResponse()

            if (response.isSuccessful && response.body() != null) {
                val product = response.body()
                if (product != null) {
                    withContext(Dispatchers.Main) {
                        Glide.with(this@ProductDetailsActivity).load(product.image)
                            .into(binding.thumbnailIv)
                        binding.titleTv.text = product.title
                        binding.descTv.text = product.description
                        binding.categoryTv.text = product.category
                        binding.ratingTv.text =
                            "${product.rating.rate} (rating Count: ${product.rating.count})"
                        binding.ratingStars.rating = product.rating.rate.toFloat()
                        binding.actualPriceTv.text = "₹${product.price}"
                        if (preferences.checkItemInCart(product)) {
                            binding.cartToggle.setImageResource(R.drawable.remove_from_cart)
                        } else {
                            binding.cartToggle.setImageResource(R.drawable.add_to_cart)
                        }
                        binding.cartToggle.visibility = View.VISIBLE
                        dismissLoading()
                        binding.cartToggle.setOnClickListener {
                            if (preferences.checkItemInCart(product)) {
                                binding.cartToggle.setImageResource(R.drawable.add_to_cart)
                                preferences.removeFromCart(product)
                            } else {
                                binding.cartToggle.setImageResource(R.drawable.remove_from_cart)
                                preferences.addToCart(product)
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "Failed to Fetch Product details!")
                withContext(Dispatchers.Main) {
                    showError()
                }
            }
        }
    }

    private fun showLoading() {
        binding.scrollView.visibility = View.GONE
        binding.loaderRl.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.loader)
    }

    private fun showError() {
        binding.scrollView.visibility = View.GONE
        binding.loaderRl.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.error)
    }

    private fun dismissLoading() {
        binding.loaderRl.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.loader)
    }
}