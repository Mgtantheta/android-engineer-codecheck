package jp.co.yumemi.android.code_check

interface GitHubRepository {
    suspend fun searchRepository(query: String): Result<List<GitHubRepositoryItem>>
}
