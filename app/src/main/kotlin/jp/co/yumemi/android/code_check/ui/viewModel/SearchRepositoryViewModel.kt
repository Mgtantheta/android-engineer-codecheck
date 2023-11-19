/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.GitHubRepositoryItem
import jp.co.yumemi.android.code_check.repository.GitHubRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchRepositoryViewModel @Inject constructor(
    private val repository: GitHubRepository
) : ViewModel() {
    private val _gitHubRepositoryItems = MutableLiveData<List<GitHubRepositoryItem>>()
    val gitHubRepositoryItems: LiveData<List<GitHubRepositoryItem>> = _gitHubRepositoryItems

    private val _error = MutableLiveData<String>()

    fun searchRepository(query: String) {
        viewModelScope.launch {
            repository.searchRepository(query).fold(
                onSuccess = {
                    _gitHubRepositoryItems.postValue(it)
                    lastSearchDate = Date()
                },
                onFailure = {
                    _error.postValue(it.message)
                }
            )
        }
    }
}
