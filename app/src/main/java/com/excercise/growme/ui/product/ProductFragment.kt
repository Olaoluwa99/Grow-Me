package com.excercise.growme.ui.product

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
import com.excercise.growme.model.SpacingItemDecorator
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingText: TextView
    private lateinit var loadingProgress: ProgressBar

    private lateinit var recyclerView: RecyclerView

    private var selectedCategoryTag = ""

    private val productsViewModel: ProductsViewModel by activityViewModels()

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.updateStatus.collect { status ->
                    when (status){
                        Constants.INACTIVE -> {
                            loadingProgress.visibility = View.GONE
                            loadingText.visibility = View.GONE
                        }
                        Constants.LOADING -> {
                            loadingProgress.visibility = View.VISIBLE
                            loadingText.visibility = View.GONE
                        }
                        Constants.FAILURE -> {
                            loadingProgress.visibility = View.GONE
                            loadingText.visibility = View.VISIBLE
                            loadingText.text = Constants.FAILURE
                        }
                        Constants.SUCCESS -> {
                            loadingProgress.visibility = View.GONE
                            loadingText.visibility = View.GONE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.products.collect { products ->
                    when (products.isEmpty()){
                        false -> {
                            if (selectedCategoryTag.isNotBlank()) productsViewModel.setupProductsForCategory(selectedCategoryTag)
                        }
                        true -> {}//mainText.text = "---"
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                productsViewModel.categoryProducts.collect { products ->
                    when (products.isEmpty()){
                        false -> {
                            println(products)
                            //mainText.text = products.toString()

                            //
                            val adapter = ProductAdapter(products, productsViewModel)
                            recyclerView.layoutManager = GridLayoutManager(context, 2)
                            recyclerView.adapter = adapter

                            val x = (resources.displayMetrics.density * 4).toInt() //converting dp to pixels
                            recyclerView.addItemDecoration(SpacingItemDecorator(x)) //setting space between items in RecyclerView

                            //
                            adapter.updateList(products)
                            //
                            //openCart.setOnClickListener { productsViewModel.addToCart(products[0]) }
                        }
                        true -> {}//mainText.text = "---"
                    }
                }
            }
        }
    }

    private fun initialization(){
        loadingText = binding.progressText
        loadingProgress = binding.progressBar
        recyclerView = binding.recyclerView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}