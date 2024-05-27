package com.kaspersky.translator.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaspersky.domain.model.WordQuerry
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.domain.model.WordsResponce
import com.kaspersky.translator.app.App
import com.kaspersky.translator.databinding.FragmentHomeBinding
import com.kaspersky.translator.presentation.adapter.HistoryItemAdapter
import com.kaspersky.translator.presentation.extensions.addOnLoadMoreListener
import com.kaspersky.translator.view_model.MyViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel: MyViewModel

    @Inject
    lateinit var historyAdapter : HistoryItemAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textEnter = binding.textEnter
        val btnTranslate = binding.btnTranslate
        val textResult = binding.textResult
        val recyclerView = binding.recyclerView

        historyAdapter = HistoryItemAdapter(emptyList()) { translation ->
            toggleFavorite(translation)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = historyAdapter


        homeViewModel.translations.observe(viewLifecycleOwner){ history ->
            historyAdapter.updateItems(history)
        }

        homeViewModel.text_result.observe(viewLifecycleOwner) {
            textResult.text = it
        }

        recyclerView.addOnLoadMoreListener {
            homeViewModel.currentIndex += homeViewModel.batchSize
            lifecycleScope.launch {
                homeViewModel.loadHistory()
            }
        }

        btnTranslate.setOnClickListener {
            val query = textEnter.text.toString().trim()

            if (!HomeFragmentValidator(query)){
                Toast.makeText(
                    context,
                    "Введите ОДНО английское слово",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {

                    val result = homeViewModel.sendWordToApi(
                        query = WordQuerry(query)
                    )

                    homeViewModel.setTextResult(result.responce)

                    homeViewModel.saveTranslation(
                        query = WordQuerry(query),
                        responce = WordsResponce(result.responce)
                    )

                    homeViewModel.loadHistory()
                } catch (e: Exception) {
                    homeViewModel.setTextResult(e.message.toString())
                }

            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun HomeFragmentValidator(query : String) : Boolean {
        if (query.isEmpty() || !query.matches(Regex("^[a-zA-Z]+$"))) {
            return false
        }
        return true
    }

    private fun toggleFavorite(translation: WordTranslation) {
        lifecycleScope.launch {
            homeViewModel.updateFavorite(translation.id, !translation.favorite)
        }
    }
}