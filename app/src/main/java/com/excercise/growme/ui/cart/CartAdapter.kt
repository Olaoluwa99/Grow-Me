package com.excercise.growme.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.Product
import com.excercise.growme.databinding.ItemCartBinding
import com.excercise.growme.model.CartProductDiffCallback
import com.excercise.growme.model.ProductDiffCallback

class CartAdapter(
    initProducts: List<CartProduct>,
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    var products = initProducts

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartProduct) {
            binding.product = product
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newProducts: List<CartProduct>) {
        val diffCallback = CartProductDiffCallback(products, newProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products = newProducts
        diffResult.dispatchUpdatesTo(this)

        // Force rebind if necessary (use sparingly)
        notifyDataSetChanged()
    }
}