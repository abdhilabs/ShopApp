package com.abdhilabs.shopapp.data.repository

import com.abdhilabs.shopapp.data.model.Products
import com.abdhilabs.shopapp.data.remote.BelanjaApi
import com.abdhilabs.shopapp.data.remote.response.ProductResponse
import com.abdhilabs.shopapp.data.remote.response.ProductsResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val api: BelanjaApi.Api) {

    fun get(onSuccess: (List<Products>) -> Unit, onError: (Throwable) -> Unit) {
        api.getProducts().enqueue(object : Callback<ProductsResponse> {

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.data?.map {
                        with(it) {
                            Products(name, price, image, id)
                        }
                    }
                    result?.let {
                        onSuccess(it)
                    }
                } else {
                    onError(Throwable("Something went wrong!"))
                }
            }

        })
    }

    fun save(products: Products, onSuccess: (Products) -> Unit, onError: (Throwable) -> Unit) {
        api.saveProduct(products).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        with(it) {
                            onSuccess(Products(name, price, image, id))
                        }
                    }
                } else {
                    onError(Throwable("Something went wrong!"))
                }
            }
        })
    }

    fun update(
        id: Int,
        products: Products,
        onSuccess: (Products) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.updateProduct(id, products).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        with(it) {
                            onSuccess(Products(name, price, image, id))
                        }
                    }
                } else {
                    onError(Throwable("Something went wrong!"))
                }
            }
        })
    }

    fun delete(id: Int, onSuccess: (Int) -> Unit, onError: (Throwable) -> Unit) {
        api.deleteProduct(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                onError(t)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    onSuccess(id)
                } else {
                    onError(Throwable("Something went wrong!"))
                }
            }
        })
    }
}