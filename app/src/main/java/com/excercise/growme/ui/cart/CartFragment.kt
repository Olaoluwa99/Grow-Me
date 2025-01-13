package com.excercise.growme.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.databinding.FragmentCartBinding
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.CartProduct
import com.excercise.growme.model.SpacingItemDecorator
import com.excercise.growme.ui.product.ProductAdapter
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingText: TextView
    private lateinit var loadingProgress: ProgressBar

    private lateinit var recyclerView: RecyclerView

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: CartAdapter
    var productsList = listOf<CartProduct>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.updateStatus.collect { status ->
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
                cartViewModel.products.collect { cartProducts ->
                    productsList = cartProducts
                    when (productsList.isEmpty()){
                        false -> {
                            println(productsList)
                            //mainText.text = cartProducts.toString()

                            //
                            adapter = CartAdapter(productsList, cartViewModel)
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = adapter

                            val x = (resources.displayMetrics.density * 4).toInt() //converting dp to pixels
                            recyclerView.addItemDecoration(SpacingItemDecorator(x)) //setting space between items in RecyclerView

                            //
                            //adapter.updateList(productsList)
                        }
                        true -> {
                            //mainText.text = "Loading..."
                            //openProducts.setOnClickListener { Toast.makeText(context, "It is Empty", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.updateStatus.collect { status ->
                    // Handle the update status value here
                    when (status) {
                        Constants.SUCCESS -> {
                            adapter.updateList(productsList)
                        }
                        else -> { /**/ }
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