package com.example.e_commercefreestoreapi.DataSource

import android.content.Context
import android.content.SharedPreferences
import com.example.e_commercefreestoreapi.DataSource.Model.ProductsResponseModelItem
import com.example.e_commercefreestoreapi.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SharePreferences(applicationContext: Context) {

    private var applicationContext: Context? = null

    private var gson: Gson
    private var sharedPreferences: SharedPreferences? = null

    init {
        this.applicationContext = applicationContext
        gson = Gson()
        val preferencesName = applicationContext.getString(R.string.app_name)
        sharedPreferences =
            applicationContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    fun putString(key: String?, value: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return sharedPreferences!!.getString(key, "")
    }

    fun putInt(key: String?, value: Int) {
        val editor = sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun getInt(key: String?): Int {
        return sharedPreferences!!.getInt(key, 0)
    }

    fun putBoolean(key: String?, value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences!!.getBoolean(key, true)
    }

    fun putInCartDataList(list: List<ProductsResponseModelItem?>?) {
        val editor = sharedPreferences!!.edit()
        val dataModelListJson = gson.toJson(list)
        editor.putString(Constants.cartList, dataModelListJson)
        editor.apply()
    }

    fun getFromCartDataList(): List<ProductsResponseModelItem> {
        val dataModelListJson = sharedPreferences!!.getString(Constants.cartList, null)
        return if (dataModelListJson != null) {
            val type: Type = object : TypeToken<ArrayList<ProductsResponseModelItem?>?>() {}.type
            gson.fromJson<List<ProductsResponseModelItem>>(dataModelListJson, type)
        } else {
            ArrayList<ProductsResponseModelItem>()
        }
    }

    fun removeFromCart(
        ProductsResponseModelItem: ProductsResponseModelItem
    ) {
        val list: MutableList<ProductsResponseModelItem> =
            getFromCartDataList() as MutableList<ProductsResponseModelItem>
        for (i in list.indices) {
            if (list[i].title.equals(ProductsResponseModelItem.title)) {
                list.removeAt(i) // Remove the item with the same path
                break // Stop after removing the first occurrence
            }
        }
        putInCartDataList(list)
    }

    fun addToCart(
        ProductsResponseModelItem: ProductsResponseModelItem
    ) {
        val list: MutableList<ProductsResponseModelItem> =
            getFromCartDataList() as MutableList<ProductsResponseModelItem>
        list.add(ProductsResponseModelItem)
        putInCartDataList(list)
    }

    fun checkItemInCart(product: ProductsResponseModelItem?): Boolean {
        val list: List<ProductsResponseModelItem> = getFromCartDataList()
        var found = false
        for (i in list.indices) {
            if (list[i] == product) {
                found = true
                break
            }
        }
        return found
    }
}