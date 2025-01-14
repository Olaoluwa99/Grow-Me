package com.excercise.growme.ui.category

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.excercise.growme.R
import com.excercise.growme.databinding.FragmentCategoryBinding
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.Category
import com.excercise.growme.databinding.DialogProgressBinding
import com.excercise.growme.model.SpacingItemDecorator
import com.excercise.growme.model.getCategoryTags
import com.excercise.growme.model.setDefinedTags
import com.excercise.growme.ui.product.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CategoryAdapter

    private var defaultCategoryTags = emptyList<String>()
    private var refinedCategoryTags = emptyList<String>()
    private var selectedCategoryTag = ""

    private var _progressDialogBinding: DialogProgressBinding? = null
    private val progressDialogBinding get() = _progressDialogBinding!!

    private lateinit var progressDialog: AlertDialog
    private lateinit var recyclerView: RecyclerView

    private var mainCategoryList = listOf<Category>()
    private val categoriesViewModel: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
        showProgressDialog("Setting up...\n\nEnsure you are connected to the internet for first time setup.")
        mainCategoryList = getCategoryList()
        setupRecyclerView()
        categoriesViewModel.fetchProducts()
        categoriesViewModel.refreshProducts()
        observeRetrievedProducts()
    }

    private fun setupRecyclerView(){
        adapter = CategoryAdapter(mainCategoryList, object : OnCategoryClickListener {
            override fun onCategoryClicked(category: Category) {
                selectedCategoryTag = category.tag
                //
                if (selectedCategoryTag.isNotBlank()){
                    findNavController().navigate(R.id.action_categoryFragment_to_productFragment, Bundle().apply {
                        putString(Constants.CATEGORY_TAG, selectedCategoryTag)
                    })
                }
            }
        })
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter

        val x = (resources.displayMetrics.density * 4).toInt()
        recyclerView.addItemDecoration(SpacingItemDecorator(x))
    }

    private fun observeRetrievedProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoriesViewModel.products.collect { products ->
                    when {
                        products.isNotEmpty() -> {
                            //
                            categoriesViewModel.assignProducts(products)
                            //
                            defaultCategoryTags = getCategoryTags(products)
                            refinedCategoryTags = setDefinedTags(defaultCategoryTags)
                            mainCategoryList = getCategoryList()
                            adapter.updateList(mainCategoryList)
                            dismissProgressDialog()
                        }
                    }
                }
            }
        }
    }

    private fun getCategoryList(): List<Category> {
        if (mainCategoryList.size > 4){
            return listOf(
                Category(
                    title = Constants.MENS_CLOTHING_CAP,
                    tag = Constants.MENS_CLOTHING,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_man_60)!!
                ),
                Category(
                    title = Constants.WOMENS_CLOTHING_CAP,
                    tag = Constants.WOMENS_CLOTHING,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_woman_60)!!
                ),
                Category(
                    title = Constants.JEWELERY_CAP,
                    tag = Constants.JEWELERY,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_auto_awesome_60)!!
                ),
                Category(
                    title = Constants.ELECTRONICS_CAP,
                    tag = Constants.ELECTRONICS,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_devices_60)!!
                ),
                Category(
                    title = Constants.OTHERS_CAP,
                    tag = Constants.OTHERS,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_category_60)!!
                )
            )
        }else{
            return listOf(
                Category(
                    title = Constants.MENS_CLOTHING_CAP,
                    tag = Constants.MENS_CLOTHING,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_man_60)!!
                ),
                Category(
                    title = Constants.WOMENS_CLOTHING_CAP,
                    tag = Constants.WOMENS_CLOTHING,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_woman_60)!!
                ),
                Category(
                    title = Constants.JEWELERY_CAP,
                    tag = Constants.JEWELERY,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_auto_awesome_60)!!
                ),
                Category(
                    title = Constants.ELECTRONICS_CAP,
                    tag = Constants.ELECTRONICS,
                    imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_devices_60)!!
                )
            )
        }
    }

    private fun initialization(){
        recyclerView = binding.recyclerView
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