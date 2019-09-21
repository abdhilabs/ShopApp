package com.abdhilabs.shopapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abdhilabs.shopapp.data.model.Products
import com.abdhilabs.shopapp.ext.toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlin.random.Random

class DetailProductActivity : AppCompatActivity() {

    companion object {

        fun editIntent(context: Context, product: Products): Intent {
            return Intent(context, DetailProductActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_EDIT, true)
            }
        }

        fun addIntent(context: Context): Intent {
            return Intent(context, DetailProductActivity::class.java)
        }

        const val REQUEST_CODE_DETAIL_PRODUCT: Int = 123
        const val RESULT_CODE_RELOAD_DATA: Int = 124
        const val EXTRA_PRODUCT = "extra_product"
        const val EXTRA_EDIT = "extra_edit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
        val data = intent.getSerializableExtra(EXTRA_PRODUCT)

        val product = if (data == null) Products() else data as Products
        render(isEdit, product)
    }

    private fun render(isEdit: Boolean, product: Products) {
        if (isEdit) {
            showProductDetail(product)
        }

        btnDeleteProduct.visibility = if (isEdit) View.VISIBLE else View.GONE
        btnDeleteProduct.setOnClickListener {
            product.id?.let { id -> actionDeleteData(id) }
        }
        btnSaveProduct.setOnClickListener {
            if (isEdit) {
                actionUpdateData(product)
            } else {
                actionSaveData()
            }
        }
    }

    private fun actionDeleteData(id: Int) {
        App.instance.repository.delete(id, {
            "data deleted successfully".toast(this@DetailProductActivity)
            setResult(RESULT_CODE_RELOAD_DATA)
            finish()
        }, {
            it.printStackTrace()
            it.message?.toast(this@DetailProductActivity)
        })
    }

    private fun actionSaveData() {
        val random = Random.nextInt(10, 100)
        val product = Products(
            etProductNameDetail.text.toString(),
            etProductPriceDetail.text.toString().toInt(),
            //Condition apabila Gambar Product kosong
            if (etProductImageDetail.text.toString().isNotEmpty())
                etProductImageDetail.text.toString()
            else
                "https://loremflickr.com/100/100?lock=$random"
        )
        App.instance.repository.save(product, {
            "data saved successfully".toast(this@DetailProductActivity)
            setResult(RESULT_CODE_RELOAD_DATA)
            finish()
        }, {
            it.printStackTrace()
            it.message?.toast(this@DetailProductActivity)
        })
    }

    private fun actionUpdateData(product: Products) {
        product.apply {
            if (etProductNameDetail.text.toString().isNotEmpty()) name =
                etProductNameDetail.text.toString()
            if (etProductPriceDetail.text.toString().isNotEmpty()) price =
                etProductPriceDetail.text.toString().toInt()

            //Condition apabila Gambar Product kosong
            image = if (etProductImageDetail.text.toString().isNotEmpty()) {
                etProductImageDetail.text.toString()
            } else {
                val random = Random.nextInt(10, 100)
                "https://loremflickr.com/100/100?lock=$random"
            }
        }

        product.id?.let { it ->
            App.instance.repository.update(it, product, {
                "data updated successfully".toast(this@DetailProductActivity)
                setResult(RESULT_CODE_RELOAD_DATA)
                finish()
            }, {
                it.printStackTrace()
                it.message?.toast(this@DetailProductActivity)
            })
        }
    }

    private fun showProductDetail(product: Products) {
        with(product) {
            etProductNameDetail.setText(name)
            etProductPriceDetail.setText(price.toString())
            etProductImageDetail.setText(image)
            Glide.with(this@DetailProductActivity).load(image).into(imgProductDetail)
        }
    }
}
