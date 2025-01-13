package com.excercise.growme.ui.category

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
import com.excercise.growme.model.SpacingItemDecorator
import com.excercise.growme.ui.product.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingText: TextView
    private lateinit var loadingProgress: ProgressBar

    private var defaultCategoryTags = emptyList<String>()
    private var refinedCategoryTags = emptyList<String>()
    private var selectedCategoryTag = ""

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
        mainCategoryList = getCategoryList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoriesViewModel.updateStatus.collect { status ->
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
                categoriesViewModel.products.collect { products ->
                    when (products.isEmpty()){
                        false -> {
                            println(products)
                            //mainText.text = products.toString()

                            //
                            categoriesViewModel.assignProducts(products)

                            defaultCategoryTags = categoriesViewModel.getCategoryTags(products)
                            refinedCategoryTags = categoriesViewModel.setDefinedTags(defaultCategoryTags)
                            selectedCategoryTag = defaultCategoryTags[0]

                            val adapter = CategoryAdapter(mainCategoryList, object : OnCategoryClickListener {
                                override fun onCategoryClicked(category: Category) {
                                    selectedCategoryTag = category.tag
                                    //
                                    if (selectedCategoryTag.isNotBlank()){
                                        findNavController().navigate(R.id.action_categoryFragment_to_productFragment, Bundle().apply {
                                            putString("categoryTag", selectedCategoryTag)
                                        })
                                    }
                                }
                            })
                            recyclerView.layoutManager = GridLayoutManager(context, 2)
                            recyclerView.adapter = adapter

                            val x = (resources.displayMetrics.density * 4).toInt()
                            recyclerView.addItemDecoration(SpacingItemDecorator(x))
                        }
                        true -> {}//mainText.text = "---"
                    }
                }
            }
        }
    }

    private fun getCategoryList(): List<Category> {
        return listOf(
            Category(
                title = "Men's clothing",
                tag = "men's clothing",
                imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_shopping_cart_16)!!
            ),
            Category(
                title = "Women's clothing",
                tag = "women's clothing",
                imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_shopping_cart_16)!!
            ),
            Category(
                title = "Jewelery",
                tag = "jewelery",
                imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_shopping_cart_16)!!
            ),
            Category(
                title = "Electronics",
                tag = "electronics",
                imageId = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_shopping_cart_16)!!
            ),
        )
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