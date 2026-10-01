package com.example.soccerappproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.soccerappproject.databinding.FragmentStartingPageBinding

class StartingPageFragment : ViewModelFragment() {
    private var _binding: FragmentStartingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnListLeague.setOnClickListener {
            findNavController().navigate(
                StartingPageFragmentDirections.actionStartingPageToLeagueList()
            )
        }

        binding.btnLeagueAbbr.setOnClickListener {
            val abbreviation = binding.etAbbrInput.text.toString().trim()
            if (abbreviation.isEmpty()) {
                binding.etAbbrInput.error = "Enter a league abbreviation, for example eng.1"
            } else {
                findNavController().navigate(
                    StartingPageFragmentDirections.actionSeasonList(abbreviation)
                )
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
