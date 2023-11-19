import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import jp.co.yumemi.android.code_check.model.GitHubRepositoryItem
import jp.co.yumemi.android.code_check.repository.GitHubRepository
import jp.co.yumemi.android.code_check.ui.viewModel.SearchRepositoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchRepositoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SearchRepositoryViewModel
    private lateinit var repository: GitHubRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GitHubRepository::class.java)
        viewModel = SearchRepositoryViewModel(repository)
    }

    @Test
    fun searchRepository_whenSuccess_postsItems() = runTest {
        val items = listOf(GitHubRepositoryItem("name", "ownerIconUrl", "language", 1, 2, 3, 4))
        `when`(repository.searchRepository(anyString())).thenReturn(Result.success(items))

        viewModel.searchRepository("query")

        advanceUntilIdle() // This will advance the time for the coroutine to complete

        Assert.assertEquals(items, viewModel.gitHubRepositoryItems.value)
    }
}
