package com.abdhilabs.shopapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abdhilabs.shopapp.R
import com.abdhilabs.shopapp.data.model.Products
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list_product.view.*

class ListProductAdapter(private val listProduct: ArrayList<Products>) :
    RecyclerView.Adapter<ListProductAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listProduct[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback
                .onItemClick(listProduct[holder.adapterPosition])
        }
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(products: Products) {
            with(itemView) {
                tvProductName.text = products.name
                tvProductPrice.text = products.price.toString()
                Glide.with(this).load(products.image).into(imgProduct)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Products)
    }
}