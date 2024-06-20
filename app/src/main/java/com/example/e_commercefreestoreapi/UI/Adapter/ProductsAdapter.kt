package com.example.e_commercefreestoreapi.UI.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModelItem
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.R
import com.example.e_commercefreestoreapi.UI.Activities.ProductDetailsActivity
import com.example.e_commercefreestoreapi.databinding.ItemProductsBinding

class ProductsAdapter(
    val context: Context,
    val preferences: SharePreferences,
    val productList: List<ProductsResponseModelItem>,
    val listener: ProductListener
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: ProductsResponseModelItem = productList[position]
        Glide.with(context).load(product.image).into(holder.binding.thumbnailIv)
        holder.binding.titleTv.text = product.title
        holder.binding.categoryTv.text = product.category
        holder.binding.ratingTv.text = "${product.rating.rate} (rating Count: ${product.rating.count})"
        holder.binding.ratingStars.rating = product.rating.rate.toFloat()
        holder.binding.actualPriceTv.text = "₹${product.price}"

        if (preferences.checkItemInCart(product)) {
            holder.binding.cartToggle.setImageResource(R.drawable.remove_from_cart)
        } else {
            holder.binding.cartToggle.setImageResource(R.drawable.add_to_cart)
        }

        holder.binding.cartToggle.setOnClickListener {
            if (preferences.checkItemInCart(product)) {
                holder.binding.cartToggle.setImageResource(R.drawable.add_to_cart)
                listener.removeItemFromCart(product)
                notifyDataSetChanged()
            } else {
                holder.binding.cartToggle.setImageResource(R.drawable.remove_from_cart)
                listener.addItemToCart(product)
                notifyDataSetChanged()
            }
        }

        holder.itemView.setOnClickListener {
            listener.viewProduct(product.id)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(val binding: ItemProductsBinding) : RecyclerView.ViewHolder(binding.root)

    interface ProductListener {
        fun addItemToCart(product: ProductsResponseModelItem)
        fun removeItemFromCart(product: ProductsResponseModelItem)
        fun viewProduct(id: Int)
    }
}
