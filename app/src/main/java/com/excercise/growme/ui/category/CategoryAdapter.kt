package com.excercise.growme.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.Category
import com.excercise.growme.data.Product
import com.excercise.growme.databinding.ItemCartBinding
import com.excercise.growme.databinding.ItemCategoryBinding
import com.excercise.growme.model.CartProductDiffCallback
import com.excercise.growme.model.CategoryDiffCallback
import com.excercise.growme.model.ProductDiffCallback

class CategoryAdapter(
    initCategory: List<Category>,
    private val clickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categories = initCategory

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateList(newCategory: List<Category>) {
        val diffCallback = CategoryDiffCallback(categories, newCategory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories = newCategory
        diffResult.dispatchUpdatesTo(this)

        // Force rebind if necessary (use sparingly)
        notifyDataSetChanged()
    }
}

interface OnCategoryClickListener {
    fun onCategoryClicked(category: Category)
}