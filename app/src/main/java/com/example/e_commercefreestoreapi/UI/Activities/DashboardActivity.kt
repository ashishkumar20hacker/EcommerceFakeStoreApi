package com.example.e_commercefreestoreapi.UI.Activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.e_commercefreestoreapi.UI.ConnectionChecker
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModelItem
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.R
import com.example.e_commercefreestoreapi.UI.Adapter.ProductsAdapter
import com.example.e_commercefreestoreapi.ViewModel.DashboardViewModel
import com.example.e_commercefreestoreapi.databinding.ActivityDashboardBinding
import com.example.e_commercefreestoreapi.databinding.FilterDialogBinding
import com.example.e_commercefreestoreapi.databinding.SortDialogBinding


class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding
    lateinit var dashboardViewModel: DashboardViewModel
    lateinit var preferences: SharePreferences
    var filterType: Int = R.id.radioAll
    var sortType: Int = R.id.radioAsc

    var mainList : List<ProductsResponseModelItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SharePreferences(this)
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setRecyclerViewData()

        binding.cart.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, CartActivity::class.java))
        }

        binding.sort.setOnClickListener {
            openSortDialog()
        }

        binding.filter.setOnClickListener {
            openFilterDialog()
        }

        binding.search.setOnClickListener {
            filterProductsByTitle(mainList,binding.searchEt.text.toString().trim())
        }

        binding.searchEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                filterProductsByTitle(mainList, binding.searchEt.text.toString().trim())
            } else {
                filterProductsByTitle(mainList, binding.searchEt.text.toString().trim())
            }
        }

        binding.searchEt.addTextChangedListener { editable ->
            if (editable.isNullOrEmpty()) {
                mainList?.let { handleAdapter(true, it) }
            }
        }

    }

    private fun filterProductsByTitle(
        list: List<ProductsResponseModelItem>?,
        searchText: String
    ) {
        if (searchText.isEmpty()) {
            list?.let { handleAdapter(true, it) }
        } else {
            handleAdapter(true, list!!.filter { it.title.contains(searchText, ignoreCase = true) })
        }
    }

    private fun setRecyclerViewData() {
        if (ConnectionChecker.isNetworkAvailable(this)) {
            showLoading()
            dashboardViewModel.getAllProducts {
                it?.let { it1 -> handleAdapter(false, it1) }
            }
        } else {
            showError()
        }
    }

    private fun handleAdapter(isFromSearch: Boolean, it: List<ProductsResponseModelItem>) {
        runOnUiThread {
            if (it.isNotEmpty()) {
                if (!isFromSearch) {
                    mainList = it
                }
                val productsAdapter = ProductsAdapter(this, preferences, it, object : ProductsAdapter.ProductListener{
                    override fun addItemToCart(product: ProductsResponseModelItem) {
                        preferences.addToCart(product)
                    }

                    override fun removeItemFromCart(product: ProductsResponseModelItem) {
                        preferences.removeFromCart(product)
                    }

                    override fun viewProduct(id: Int) {
                        startActivity(Intent(this@DashboardActivity, ProductDetailsActivity::class.java).putExtra("productId", id))
                    }
                })
                binding.productsRv.setAdapter(productsAdapter)
                dismissLoading()
            } else {
                showError()
            }
        }
    }

    private fun openFilterDialog() {
        val binding = FilterDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(this@DashboardActivity, R.style.SheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.getRoot())
        when (filterType) {
            R.id.radioElectronics -> {
                binding.radioElectronics.setChecked(true)
            }
            R.id.radioJewelery -> {
                binding.radioJewelery.setChecked(true)
            }
            R.id.radioMensClothing -> {
                binding.radioMensClothing.setChecked(true)
            }
            R.id.radioWomenClothing -> {
                binding.radioWomenClothing.setChecked(true)
            }
            else -> {
                binding.radioAll.setChecked(true)
            }
        }
        binding.applyBt.setOnClickListener { v ->
            filterType = binding.radioGroupStatus.getCheckedRadioButtonId()
            when (filterType) {
                R.id.radioElectronics -> {
                    showLoading()
                    dashboardViewModel.getFilteredProductList("electronics") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                R.id.radioJewelery -> {
                    showLoading()
                    dashboardViewModel.getFilteredProductList("jewelery") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                R.id.radioMensClothing -> {
                    showLoading()
                    dashboardViewModel.getFilteredProductList("men's clothing") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                R.id.radioWomenClothing -> {
                    showLoading()
                    dashboardViewModel.getFilteredProductList("women's clothing") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                else -> {
                    setRecyclerViewData()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openSortDialog() {
        val binding = SortDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(this@DashboardActivity, R.style.SheetDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.getRoot())
        if (sortType == R.id.radioAsc) {
            binding.radioAsc.setChecked(true)
        } else if (sortType == R.id.radioDesc) {
            binding.radioDesc.setChecked(true)
        }
        binding.applyBt.setOnClickListener { v ->
            sortType = binding.radioGroupStatus.getCheckedRadioButtonId()
            when (sortType) {
                R.id.radioAsc -> {
                    showLoading()
                    dashboardViewModel.getSortedProductList("asc") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                R.id.radioDesc -> {
                    showLoading()
                    dashboardViewModel.getSortedProductList("desc") {
                        it?.let { it1 -> handleAdapter(false, it1) }
                    }
                }
                else -> {
                    setRecyclerViewData()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading() {
        binding.productsRv.visibility = View.GONE
        binding.loaderRl.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.loader)
    }

    private fun showError() {
        binding.productsRv.visibility = View.GONE
        binding.loaderRl.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.error)
    }

    private fun dismissLoading() {
        binding.loaderRl.visibility = View.GONE
        binding.productsRv.visibility = View.VISIBLE
        binding.lottieAnimationView.setAnimation(R.raw.loader)
    }
}