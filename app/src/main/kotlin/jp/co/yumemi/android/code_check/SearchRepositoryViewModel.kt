/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.async
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.util.*

const val BASE_URL = "https://api.github.com/search/repositories"
const val ACCEPT_HEADER = "application/vnd.github.v3+json"
class SearchRepositoryViewModel() : ViewModel() {
    suspend fun searchResults(inputText: String): List<item> {
        val client = HttpClient(Android)

        return viewModelScope.async {
            val response: HttpResponse = client.get(BASE_URL) {
                header("Accept", ACCEPT_HEADER)
                parameter("q", inputText)
            }

            val jsonBody = JSONObject(response.receive<String>())
            val jsonItems = jsonBody.optJSONArray("items")!!

            val items = mutableListOf<item>()

            for (i in 0 until jsonItems.length()) {
                val jsonItem = jsonItems.optJSONObject(i)!!
                val name = jsonItem.optString("full_name")
                val ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url")
                val language = jsonItem.optString("language")
                val stargazersCount = jsonItem.optLong("stargazers_count")
                val watchersCount = jsonItem.optLong("watchers_count")
                val forksCount = jsonItem.optLong("forks_count")
                val openIssuesCount = jsonItem.optLong("open_issues_count")

                items.add(
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

            lastSearchDate = Date()

            return@async items.toList()
        }.await()
    }
}

//TODO: このクラスは別ファイルに切り出す
// TODO: 名前をGithubRepositoryItemに変更したいが、エラーが出てて変更できないため、一旦itemのままにしておく
@Parcelize
data class item(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
