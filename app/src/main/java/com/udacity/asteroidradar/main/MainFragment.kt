package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.detail.DetailFragment
import com.udacity.asteroidradar.detail.DetailFragmentArgs
import com.udacity.asteroidradar.model.source.AsteroidRepository
import com.udacity.asteroidradar.model.source.local.AsteroidDatabase
import com.udacity.asteroidradar.model.source.local.AsteroidLocalDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidRemoteDataSource
import com.udacity.asteroidradar.model.source.remote.AsteroidService

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        initViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        val adapter = AsteroidAdapter {
            viewModel.onAsteroidClicked(it)
        }

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToDetails.observe(viewLifecycleOwner) {
           it?.let {
               this.findNavController().navigate(
                   MainFragmentDirections.actionShowDetail(it)
               )
               viewModel.onNavigationDone()
           }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun initViewModel() {
        val localDataSource =
            AsteroidLocalDataSource(AsteroidDatabase.getInstance(requireContext()).asteriodDao)
        val remoteDataSource = AsteroidRemoteDataSource(AsteroidService())
        val repository = AsteroidRepository(localDataSource, remoteDataSource)

        viewModel = ViewModelProvider(
            this,
            AsteroidViewModelFactory(repository)
        ).get(MainViewModel::class.java)
    }
}
