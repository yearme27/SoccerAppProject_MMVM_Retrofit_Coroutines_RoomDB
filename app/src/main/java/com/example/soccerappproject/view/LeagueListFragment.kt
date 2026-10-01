package com.example.soccerappproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.soccerappproject.databinding.FragmentLeaguesListBinding
import com.example.soccerappproject.model.LeagueResponse
import com.example.soccerappproject.model.UIState

class LeagueListFragment : ViewModelFragment() {
    private var _binding: FragmentLeaguesListBinding? = null
    private val binding get() = _binding!!

    private val soccerAdapter by lazy {
        SoccerAdapter(openSeason = ::openSeason)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaguesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLeagues.adapter = soccerAdapter

        viewModel.allLeagueListData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvLoadingText.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvLoadingText.visibility = View.VISIBLE
                    binding.tvLoadingText.text = uiState.error.localizedMessage ?: "Something went wrong"
                }
                is UIState.Success<*> -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvLoadingText.visibility = View.GONE
                    soccerAdapter.setLeagueList((uiState.response as LeagueResponse).data)
                }
            }
        }

        viewModel.loadLeagues()
    }

    private fun openSeason(id: String) {
        findNavController().navigate(
            LeagueListFragmentDirections.actionLeagueListToSeasonList(id)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
