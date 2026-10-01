package com.example.soccerappproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.soccerappproject.databinding.FragmentSeasonListBinding
import com.example.soccerappproject.model.SeasonResponse
import com.example.soccerappproject.model.UIState

class SeasonListFragment : ViewModelFragment() {
    private var _binding: FragmentSeasonListBinding? = null
    private val binding get() = _binding!!

    private val seasonAdapter by lazy {
        SeasonAdapter(openStanding = ::openStanding)
    }

    private val args: SeasonListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSeason.adapter = seasonAdapter

        viewModel.allSeasonListData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.pbLoadById.visibility = View.VISIBLE
                    binding.tvErrorTextSeason.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.pbLoadById.visibility = View.GONE
                    binding.tvErrorTextSeason.visibility = View.VISIBLE
                    binding.tvErrorTextSeason.text = uiState.error.localizedMessage ?: "Something went wrong"
                }
                is UIState.Success<*> -> {
                    binding.pbLoadById.visibility = View.GONE
                    binding.tvErrorTextSeason.visibility = View.GONE
                    seasonAdapter.setSeasonList((uiState.response as SeasonResponse).data.seasons)
                }
            }
        }

        viewModel.loadSeasons(args.leagueId)
    }

    private fun openStanding(season: Int) {
        findNavController().navigate(
            SeasonListFragmentDirections.actionSeasonListToStanding(args.leagueId, season)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
