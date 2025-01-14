package com.excercise.growme.ui.product

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.R
import com.excercise.growme.databinding.FragmentProductBinding
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.Product
import com.excercise.growme.databinding.DialogProgressBinding
import com.excercise.growme.model.SpacingItemDecorator
import com.excercise.growme.ui.cart.CartAdapter
import com.excercise.growme.ui.showShortSnackbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private var _progressDialogBinding: DialogProgressBinding? = null
    private val progressDialogBinding get() = _progressDialogBinding!!

    private lateinit var progressDialog: AlertDialog
    private lateinit var recyclerView: RecyclerView

    private var selectedCategoryTag = ""

    private val productsViewModel: ProductsViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter
    private var productsList = listOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCategoryTag = requireArguments().getString(Constants.CATEGORY_TAG).toString()

        initialization()

        //
        adapter = ProductAdapter(productsList, productsViewModel)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter

        val x = (resources.displayMetrics.density * 4).toInt() //converting dp to pixels
        recyclerView.addItemDecoration(SpacingItemDecorator(x)) //setting space between items in RecyclerView

        //
        productsViewModel.fetchProducts()
        productsViewModel.fetchCartProducts()
        productsViewModel.refreshProducts()

        //
        observeAllRetrievedProducts()
        setupRecyclerView()
        observeAddToCartStatus(view)
    }

    private fun initialization(){
        recyclerView = binding.recyclerView
    }

    private fun observeAllRetrievedProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.products.collect { products ->
                    when {
                        products.isNotEmpty() -> {
                            if (selectedCategoryTag.isNotBlank()) productsViewModel.setupProductsForCategory(selectedCategoryTag)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                productsViewModel.categoryProducts.collect { products ->
                    productsList = products
                    when {
                        products.isNotEmpty() -> {
                            adapter.updateList(productsList)
                        }
                    }
                }
            }
        }
    }

    private fun observeAddToCartStatus(view: View){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.addToCartStatus.collect { status ->
                    when (status){
                        Constants.INACTIVE -> { /**/ }
                        Constants.LOADING -> {
                            showProgressDialog("Loading...")
                        }
                        Constants.FAILURE -> {
                            dismissProgressDialog()
                            showShortSnackbar(view, "Failed to add item to cart")
                            productsViewModel.resetCartStatus()
                        }
                        Constants.SUCCESS -> {
                            dismissProgressDialog()//
                            productsList = productsViewModel.categoryProducts.value
                            adapter.updateList(productsList)
                            showShortSnackbar(view, "Added to Cart")
                            productsViewModel.resetCartStatus()
                        }
                        Constants.DUPLICATE -> {
                            dismissProgressDialog()
                            showShortSnackbar(view, "Product is already in Cart.")
                            productsViewModel.resetCartStatus()
                        }
                    }
                }
            }
        }
    }

    private fun showProgressDialog(message: String) {
        _progressDialogBinding = DialogProgressBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(progressDialogBinding.root)
        builder.setCancelable(false)

        //
        progressDialogBinding.progressText.text = message

        progressDialog = builder.create()
        progressDialog.show()
    }

    private fun dismissProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissProgressDialog()
        _binding = null
        _progressDialogBinding = null
    }
}