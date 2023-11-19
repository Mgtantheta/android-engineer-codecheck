/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import GitHubRepositoryImpl
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.android.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.launch
import java.util.*

class SearchRepositoryViewModel() : ViewModel() {
    private val client = HttpClient(Android)
    private val api: GitHubApi = GitHubApiImpl(client)
    private val repository: GitHubRepository = GitHubRepositoryImpl(api)

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

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}
