package com.smart.movieslist.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.smart.movieslist.R
import com.smart.movieslist.data.model.GenresResponse
import com.smart.movieslist.databinding.FragmentHomeBinding
import com.smart.movieslist.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPagerAdapter: MoviesViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            viewModel.getGenres()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.backgroundColor)
        binding = FragmentHomeBinding.inflate(inflater)
        binding.retryButton.setOnClickListener { viewModel.getGenres() }
        binding.bindSearch()
        binding.bindGenre()
        return binding.root
    }

    private fun FragmentHomeBinding.bindGenre() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { uiStates: UiStates ->
                loadingProgress.isVisible = false
                retryButton.isVisible = false
                errorMsg.isVisible = false
                when (uiStates) {
                    is UiStates.Error -> {
                        onError(uiStates)
                    }
                    UiStates.Loading -> {
                        loadingProgress.isVisible = true
                    }
                    is UiStates.Success<*> -> {
                        val response = uiStates.data as GenresResponse
                        viewPagerAdapter =
                            MoviesViewPagerAdapter(this@HomeFragment, response.genresList)
                        pager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, pager) { tab, position ->
                            tab.text = response.genresList[position].name
                        }.attach()
                    }
                }
            }
        }
    }


    private fun FragmentHomeBinding.onError(uiStates: UiStates.Error) {
        when (uiStates.exception.getErrorType()) {
            // Do what you want in case of
            Constants.ErrorType.NETWORK -> {
            // No internet connection
            }
            Constants.ErrorType.TIMEOUT -> {
            // Request time out
            }
            Constants.ErrorType.UNKNOWN -> {}
        }
        retryButton.isVisible = true
        errorMsg.isVisible = true
        errorMsg.text = uiStates.exception
            .getErrorType()
            .getMessage(requireContext())
        requireContext().showLongToast(
            "\uD83D\uDE28 Wooops ${
                uiStates.exception.getErrorType().getMessage(requireContext())
            }"
        )
    }


    private fun FragmentHomeBinding.bindSearch(
    ) {
        searchET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateMovieListFromInput()
                true
            } else {
                false
            }
        }
        searchET.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateMovieListFromInput()
                true
            } else {
                false
            }
        }

    }

    private fun FragmentHomeBinding.updateMovieListFromInput() {
        searchET.text.trim().let {
            searchET.setText("")
            searchET.clearFocus();
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    it.toString()
                )
            )
        }
    }

}

