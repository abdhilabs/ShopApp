package com.abdhilabs.shopapp.data.model

import java.io.Serializable

data class Products(
    var name: String = "",
    var price: Int = 0,
    var image: String = "",
    var id: Int? = null
) : Serializable