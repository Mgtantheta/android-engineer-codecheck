package jp.co.yumemi.android.code_check

import GitHubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideHttpClient(): HttpClient = HttpClient(Android)

    @Provides
    @ViewModelScoped
    fun provideGitHubApi(client: HttpClient): GitHubApi = GitHubApiImpl(client)

    @Provides
    @ViewModelScoped
    fun provideGitHubRepository(api: GitHubApi): GitHubRepository = GitHubRepositoryImpl(api)
}
