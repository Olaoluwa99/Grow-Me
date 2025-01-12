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
import com.excercise.growme.databinding.FragmentCartBinding
import com.excercise.growme.constants.Constants
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var openCategories: Button
    private lateinit var openProducts: Button
    private lateinit var mainText: TextView

    private lateinit var loadingText: TextView
    private lateinit var loadingProgress: ProgressBar

    private val cartViewModel: CartViewModel by activityViewModels()

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
                    when (cartProducts.isEmpty()){
                        false -> {
                            println(cartProducts)
                            mainText.text = cartProducts.toString()

                            openProducts.setOnClickListener {
                                cartViewModel.removeFromCart(cartProducts[0])
                            }
                        }
                        true -> {
                            mainText.text = "Loading..."
                            openProducts.setOnClickListener { Toast.makeText(context, "It is Empty", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            }
        }
    }

    private fun initialization(){
        openCategories = binding.openProducts
        openProducts = binding.openProducts
        mainText = binding.text
        loadingText = binding.progressText
        loadingProgress = binding.progressBar
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}