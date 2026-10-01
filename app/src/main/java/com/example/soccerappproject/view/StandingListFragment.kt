package com.example.soccerappproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.soccerappproject.databinding.FragmentStandingListBinding
import com.example.soccerappproject.model.StandingResponse
import com.example.soccerappproject.model.UIState

class StandingListFragment : ViewModelFragment() {
    private var _binding: FragmentStandingListBinding? = null
    private val binding get() = _binding!!

    private val standingAdapter by lazy {
        StandingAdapter()
    }

    private val args: StandingListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStandingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvStanding.adapter = standingAdapter

        viewModel.allStandingListData.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    binding.pbLoadStanding.visibility = View.VISIBLE
                    binding.tvErrorTextStanding.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.pbLoadStanding.visibility = View.GONE
                    binding.tvErrorTextStanding.visibility = View.VISIBLE
                    binding.tvErrorTextStanding.text = uiState.error.localizedMessage ?: "Something went wrong"
                }
                is UIState.Success<*> -> {
                    binding.pbLoadStanding.visibility = View.GONE
                    binding.tvErrorTextStanding.visibility = View.GONE
                    standingAdapter.setStandingList((uiState.response as StandingResponse).data.standings)
                }
            }
        }

        viewModel.loadStandings(args.seasonYear, args.leagueId)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
