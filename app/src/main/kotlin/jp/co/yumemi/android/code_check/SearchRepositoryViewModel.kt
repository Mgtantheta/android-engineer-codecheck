/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

const val BASE_URL = "https://api.github.com/search/repositories"
const val ACCEPT_HEADER = "application/vnd.github.v3+json"

class SearchRepositoryViewModel() : ViewModel() {
    private val client: HttpClient = HttpClient(Android)
    private val _items = MutableLiveData<List<item>>()
    val items: LiveData<List<item>> = _items

    fun searchResults(inputText: String) {
        viewModelScope.launch {
            try {
                val response: HttpResponse = client.get(BASE_URL) {
                    header("Accept", ACCEPT_HEADER)
                    parameter("q", inputText)
                }

                val jsonBody = JSONObject(response.receive<String>())
                val jsonItems: JSONArray? = jsonBody.optJSONArray("items")

                val newItems = mutableListOf<item>()
                jsonItems?.let { itemArray ->
                    for (i in 0 until itemArray.length()) {
                        val jsonItem: JSONObject? = itemArray.optJSONObject(i)
                        val name = jsonItem?.optString("full_name") ?: ""
                        val ownerIconUrl =
                            jsonItem?.optJSONObject("owner")?.optString("avatar_url") ?: ""
                        val language = jsonItem?.optString("language") ?: ""
                        val stargazersCount = jsonItem?.optLong("stargazers_count") ?: 0
                        val watchersCount = jsonItem?.optLong("watchers_count") ?: 0
                        val forksCount = jsonItem?.optLong("forks_count") ?: 0
                        val openIssuesCount = jsonItem?.optLong("open_issues_count") ?: 0
                        newItems.add(
                            item(
                                name = name,
                                ownerIconUrl = ownerIconUrl,
                                language = language,
                                stargazersCount = stargazersCount,
                                watchersCount = watchersCount,
                                forksCount = forksCount,
                                openIssuesCount = openIssuesCount
                            )
                        )
                    }
                }

                _items.postValue(newItems)
                lastSearchDate = Date()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}
