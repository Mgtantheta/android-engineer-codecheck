/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.SearchListAdapter
import jp.co.yumemi.android.code_check.databinding.FragmentSearchRepositoryBinding
import jp.co.yumemi.android.code_check.model.GitHubRepositoryItem
import jp.co.yumemi.android.code_check.ui.viewModel.SearchRepositoryViewModel

@AndroidEntryPoint
class SearchRepositoryFragment : Fragment() {
    private val viewModel: SearchRepositoryViewModel by viewModels()
    private var _binding: FragmentSearchRepositoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val adapter = setupAdapter()
        setupRecyclerView(binding.recyclerView, adapter)
        setSearch(binding.searchInputText)
        viewModel.gitHubRepositoryItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupAdapter(): SearchListAdapter {
        return SearchListAdapter(object : SearchListAdapter.OnItemClickListener {
            override fun itemClick(item: GitHubRepositoryItem) {
                gotoRepositoryFragment(item)
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        val layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
    }

    private fun setSearch(editText: EditText) {
        editText.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = editText.text.toString()
                if (searchText.isNotEmpty()) {
                    performSearch(searchText)
                }
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchRepository(query)
    }

    private fun gotoRepositoryFragment(item: GitHubRepositoryItem) {
        val action =
            SearchRepositoryFragmentDirections.actionSearchRepositoryFragmentToDetailRepositoryFragment(item)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

