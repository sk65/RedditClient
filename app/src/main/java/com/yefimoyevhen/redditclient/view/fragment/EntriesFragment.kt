package com.yefimoyevhen.redditclient.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yefimoyevhen.redditclient.R
import com.yefimoyevhen.redditclient.adapter.EntriesAdapter
import com.yefimoyevhen.redditclient.databinding.FragmentEntriesBinding
import com.yefimoyevhen.redditclient.util.Resource
import com.yefimoyevhen.redditclient.util.URL_ARGUMENT_KEY
import com.yefimoyevhen.redditclient.viewModel.RedditViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EntriesFragment : Fragment() {

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private var _binding: FragmentEntriesBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var entriesAdapter: EntriesAdapter

    private lateinit var redditViewModel: RedditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        redditViewModel = ViewModelProvider(requireActivity()).get(RedditViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()


        redditViewModel.resourceLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    isError = false
                    hideProgressBar()
                    entriesAdapter.differ.submitList(resource.data)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    isError = true
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        entriesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(URL_ARGUMENT_KEY, it)
            }
            findNavController().navigate(
                R.id.action_entriesFragment_to_detailsEntryFragment,
                bundle
            )
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = entriesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@EntriesFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 1
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                redditViewModel.fetchData()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
}