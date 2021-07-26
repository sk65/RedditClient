package com.yefimoyevhen.redditclient.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
                    hideProgressBar()
                    entriesAdapter.differ.submitList(resource.data)
                }
                is Resource.Error -> {
                    hideProgressBar()
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
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = entriesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}