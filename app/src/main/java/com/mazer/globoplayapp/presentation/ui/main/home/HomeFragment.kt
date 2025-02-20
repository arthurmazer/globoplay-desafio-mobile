package com.mazer.globoplayapp.presentation.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mazer.globoplayapp.databinding.FragmentHomeBinding
import com.mazer.globoplayapp.domain.entities.Movie
import com.mazer.globoplayapp.presentation.adapter.CarouselMoviesAdapter
import com.mazer.globoplayapp.presentation.ui.details.MovieDetailsActivity
import com.mazer.globoplayapp.utils.AppConstants
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModel()
    private lateinit var adapterPopularMovies: CarouselMoviesAdapter
    private lateinit var adapterTopRatedMovies: CarouselMoviesAdapter
    private lateinit var adapterUpcomingMovies: CarouselMoviesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        registerObservers()
    }

    private fun setupView() {
        adapterPopularMovies = CarouselMoviesAdapter {
            goToMovieDetailsScreen(it)
        }
        adapterTopRatedMovies = CarouselMoviesAdapter {
            goToMovieDetailsScreen(it)
        }
        adapterUpcomingMovies = CarouselMoviesAdapter {
            goToMovieDetailsScreen(it)
        }

        setupPopularMoviesCarousel()
        setupTopRatedMoviesCarousel()
        setupUpcomingMoviesCarousel()
    }

    private fun goToMovieDetailsScreen(movie: Movie){
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(AppConstants.MOVIE_EXTRA, movie)
        startActivity(intent)
    }

    private fun setupPopularMoviesCarousel() {
        binding.carouselViewPopular.setAdapter(adapterPopularMovies)
        binding.carouselViewPopular.setOnLoadMoreListener {
            viewModel.loadPopularMovies(it)

        }
    }

    private fun setupTopRatedMoviesCarousel() {
        binding.carouselViewTopRated.setAdapter(adapterTopRatedMovies)
        binding.carouselViewTopRated.setOnLoadMoreListener {
            viewModel.loadTopRatedMovies(it)

        }
    }

    private fun setupUpcomingMoviesCarousel() {
        binding.carouselViewUpcoming.setAdapter(adapterUpcomingMovies)
        binding.carouselViewUpcoming.setOnLoadMoreListener {
            viewModel.loadUpcomingMovies(it)

        }
    }

    private fun registerObservers() {
        viewModel.moviePopularList.observe(viewLifecycleOwner) {
            adapterPopularMovies.addList(it)
            binding.carouselViewPopular.setDataLoaded()
            showLoadingShimmer(false)
        }
        viewModel.movieTopRatedList.observe(viewLifecycleOwner) {
            adapterTopRatedMovies.addList( it)
            binding.carouselViewTopRated.setDataLoaded()
            showLoadingShimmer(false)
        }
        viewModel.movieUpcomingList.observe(viewLifecycleOwner) {
            adapterUpcomingMovies.addList(it)
            binding.carouselViewUpcoming.setDataLoaded()
            showLoadingShimmer(false)
        }
    }

    private fun showLoadingShimmer(showLoading: Boolean){
        if (showLoading){
            binding.shimmerViewLayout.startShimmer()
            binding.shimmerViewLayout.visibility = View.VISIBLE
        } else{
            binding.shimmerViewLayout.visibility = View.GONE
            binding.shimmerViewLayout.stopShimmer()
        }
        showData(showLoading)
    }

    private fun showData(isLoading: Boolean){
        binding.layoutHomeData.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

}