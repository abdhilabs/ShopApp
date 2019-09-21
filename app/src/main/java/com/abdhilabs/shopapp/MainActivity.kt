package com.abdhilabs.shopapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdhilabs.shopapp.adapter.ListProductAdapter
import com.abdhilabs.shopapp.data.model.Products
import com.abdhilabs.shopapp.ext.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listProductAdapter: ListProductAdapter? = null
    private var list: ArrayList<Products> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showListProduct()
        setupAddProduct()
        setListClickAction()
    }

    //Show list
    //pick from App
    private fun showListProduct() {
        App.instance.repository.get({
            list.addAll(it)

            listProductAdapter = ListProductAdapter(list)

            rvProducts.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = listProductAdapter
                setHasFixedSize(true)
            }

            setListClickAction()
        }, {
            it.printStackTrace()
            it.message?.toast(this)
        })
    }

    private fun setupAddProduct() {
        btnAddProduct.setOnClickListener {
            startActivityForResult(
                DetailProductActivity.addIntent(this@MainActivity),
                DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT
            )
        }
    }

    private fun setListClickAction() {
        listProductAdapter?.setOnItemClickCallback(
            object : ListProductAdapter.OnItemClickCallback {
                override fun onItemClick(data: Products) {
                    startActivityForResult(
                        DetailProductActivity.editIntent(this@MainActivity, data),
                        DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT
                    )
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DetailProductActivity.REQUEST_CODE_DETAIL_PRODUCT &&
            resultCode == DetailProductActivity.RESULT_CODE_RELOAD_DATA
        ) {
            list = arrayListOf()
            showListProduct()
        }
    }
}
