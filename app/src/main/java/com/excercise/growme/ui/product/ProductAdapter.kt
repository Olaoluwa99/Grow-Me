package com.excercise.growme.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.data.Product
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.excercise.growme.databinding.ItemProductBinding
import com.excercise.growme.model.ProductDiffCallback

class ProductAdapter(
    initProducts: List<Product>,
    private val viewModel: ProductsViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    var products = initProducts

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newProducts: List<Product>) {
        val diffCallback = ProductDiffCallback(products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products = newProducts
        diffResult.dispatchUpdatesTo(this)
    }
}
