package com.smart.movieslist.ui.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smart.movieslist.R
import com.smart.movieslist.data.model.MovieDetailsResponse
import com.smart.movieslist.databinding.MovieDetailsFragmentBinding
import com.smart.movieslist.utils.UiStates
import com.smart.movieslist.utils.getErrorType
import com.smart.movieslist.utils.getMessage
import com.smart.movieslist.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var binding: MovieDetailsFragmentBinding
    private val args: MovieDetailsFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            viewModel.getMovieDetails(args.movieId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.backgroundColor)
        binding = MovieDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retryButton.setOnClickListener { viewModel.getMovieDetails(args.movieId) }
        binding.onBackPressed = { findNavController().navigateUp() }
        binding.bindMovieDetails()
    }

    private fun MovieDetailsFragmentBinding.bindMovieDetails() {
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
                        binding.loadingProgress.isVisible = true
                    }
                    is UiStates.Success<*> -> {
                        val response = uiStates.data as MovieDetailsResponse
                        binding.movie = response
                    }
                }
            }
        }
    }

    private fun MovieDetailsFragmentBinding.onError(uiStates: UiStates.Error) {
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
}