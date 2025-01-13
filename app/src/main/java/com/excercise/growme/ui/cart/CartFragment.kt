package com.excercise.growme.ui.cart

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.R
import com.excercise.growme.databinding.FragmentCartBinding
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.CartProduct
import com.excercise.growme.databinding.DialogProgressBinding
import com.excercise.growme.model.SpacingItemDecorator
import com.excercise.growme.ui.product.ProductAdapter
import com.excercise.growme.ui.showShortSnackbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private var _progressDialogBinding: DialogProgressBinding? = null
    private val progressDialogBinding get() = _progressDialogBinding!!

    private lateinit var progressDialog: AlertDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyCartText: TextView
    private lateinit var fab: ExtendedFloatingActionButton

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: CartAdapter
    private var productsList = listOf<CartProduct>()

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
        //
        adapter = CartAdapter(productsList, cartViewModel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val x = (resources.displayMetrics.density * 4).toInt()
        recyclerView.addItemDecoration(SpacingItemDecorator(x))
        cartViewModel.fetchCartProducts()

        fab.setOnClickListener { cartViewModel.checkout() }

        //
        observeClearStatus(view)
        observeRemoveStatus(view)
        setupRecyclerView()
    }

    private fun initialization(){
        recyclerView = binding.recyclerView
        emptyCartText = binding.emptyCartText
        fab = binding.fab
    }

    private fun setupRecyclerView(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                cartViewModel.cartProducts.collect { cartProducts ->
                    productsList = cartProducts
                    when (productsList.isEmpty()){
                        false -> {
                            adapter.updateList(productsList)
                            emptyCartText.visibility = View.GONE
                            fab.visibility = View.VISIBLE
                        }
                        true -> {
                            adapter.updateList(productsList)
                            emptyCartText.visibility = View.VISIBLE
                            fab.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun observeRemoveStatus(view: View){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.removeStatus.collect { status ->
                    when (status){
                        Constants.INACTIVE -> { /**/ }
                        Constants.LOADING -> {
                            showProgressDialog("Loading...")
                        }
                        Constants.FAILURE -> {
                            dismissProgressDialog()
                            showShortSnackbar(view, "Failed to remove item")
                            cartViewModel.resetRemoveStatus()
                        }
                        Constants.SUCCESS -> {
                            dismissProgressDialog()//
                            productsList = cartViewModel.cartProducts.value
                            adapter.updateList(productsList)
                            showShortSnackbar(view, "Removed from Cart")
                            cartViewModel.resetRemoveStatus()
                        }
                    }
                }
            }
        }
    }

    private fun observeClearStatus(view: View){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.clearStatus.collect { status ->
                    when (status){
                        Constants.INACTIVE -> { /**/ }
                        Constants.LOADING -> {
                            showProgressDialog("Checking out...")
                        }
                        Constants.FAILURE -> {
                            dismissProgressDialog()
                            showShortSnackbar(view, "Failed to Checkout items")
                            cartViewModel.resetClearStatus()
                        }
                        Constants.SUCCESS -> {
                            dismissProgressDialog()//
                            productsList = emptyList()
                            findNavController().popBackStack(R.id.categoryFragment, false)
                            cartViewModel.resetClearStatus()
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