package com.abdhilabs.shopapp.data.remote

import com.abdhilabs.shopapp.BuildConfig
import com.abdhilabs.shopapp.data.model.Products
import com.abdhilabs.shopapp.data.remote.response.ProductResponse
import com.abdhilabs.shopapp.data.remote.response.ProductsResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object BelanjaApi {

    fun create(): Api {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
        return retrofit.create(Api::class.java)
    }

    interface Api {
        @GET("/products")
        fun getProducts(): Call<ProductsResponse>

        @POST("/products")
        fun saveProduct(@Body product: Products): Call<ProductResponse>

        @DELETE("/products/{id}")
        fun deleteProduct(@Path("id") id: Int): Call<JsonObject>

        @PUT("/products/{id}")
        fun updateProduct(@Path("id") id: Int, @Body product: Products): Call<ProductResponse>
    }
}