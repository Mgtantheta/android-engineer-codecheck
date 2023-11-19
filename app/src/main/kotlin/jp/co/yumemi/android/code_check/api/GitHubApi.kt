package jp.co.yumemi.android.code_check.api

import org.json.JSONObject

interface GitHubApi {
    suspend fun searchRepository(query: String): Result<JSONObject>
}
