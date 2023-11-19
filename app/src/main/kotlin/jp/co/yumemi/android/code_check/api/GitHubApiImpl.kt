package jp.co.yumemi.android.code_check.api

import io.ktor.client.*
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.util.Constant
import org.json.JSONObject

class GitHubApiImpl(private val client: HttpClient) : GitHubApi {
    override suspend fun searchRepository(query: String): Result<JSONObject> {
        return runCatching {
            val response = client.get<HttpResponse>(Constant.BASE_URL) {
                header("Accept", Constant.ACCEPT_HEADER)
                parameter("q", query)
            }
            val jsonBody = JSONObject(response.receive<String>())
            Result.success(jsonBody)
        }.getOrElse {
            Result.failure(it)
        }
    }
}
