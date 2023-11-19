package jp.co.yumemi.android.code_check.repository

import jp.co.yumemi.android.code_check.model.GitHubRepositoryItem

interface GitHubRepository {
    suspend fun searchRepository(query: String): Result<List<GitHubRepositoryItem>>
}
