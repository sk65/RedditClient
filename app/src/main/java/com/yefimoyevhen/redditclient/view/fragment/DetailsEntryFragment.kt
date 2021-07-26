package com.yefimoyevhen.redditclient.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.yefimoyevhen.redditclient.databinding.FragmentDetailsEntryBinding

class DetailsEntryFragment : Fragment() {

    private var _binding: FragmentDetailsEntryBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsEntryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            loadUrl(args.url)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}