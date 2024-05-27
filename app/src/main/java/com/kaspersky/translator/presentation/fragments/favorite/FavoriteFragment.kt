package com.kaspersky.translator.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.translator.app.App
import com.kaspersky.translator.databinding.FragmentFavoriteBinding
import com.kaspersky.translator.presentation.adapter.HistoryItemAdapter
import com.kaspersky.translator.presentation.extensions.addOnLoadMoreListener
import com.kaspersky.translator.view_model.MyViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: MyViewModel

    @Inject
    lateinit var historyAdapter: HistoryItemAdapter

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)

        lifecycleScope.launch {
            homeViewModel.loadFavoriteHistory()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerView

        historyAdapter = HistoryItemAdapter(emptyList()) { translation ->
            toggleFavorite(translation)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = historyAdapter


        homeViewModel.favoriteTranslations.observe(viewLifecycleOwner) { history ->
            historyAdapter.updateItems(history)
        }

        recyclerView.addOnLoadMoreListener {
            homeViewModel.currentIndexFavorite += homeViewModel.batchSizeFavorite
            lifecycleScope.launch {
                homeViewModel.loadFavoriteHistory()
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleFavorite(translation: WordTranslation) {
        lifecycleScope.launch {
            homeViewModel.updateFavorite(translation.id, !translation.favorite)
        }
    }
}