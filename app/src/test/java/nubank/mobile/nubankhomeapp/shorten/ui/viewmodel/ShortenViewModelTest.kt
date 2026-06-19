package nubank.mobile.nubankhomeapp.shorten.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nubank.mobile.nubankhomeapp.shorten.ORIGINAL_URL_STUB
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.ui.model.SearchBarState as State
import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProvider
import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel
import nubank.mobile.nubankhomeapp.shorten.ALIAS_UI_MODEL_STUB
import nubank.mobile.nubankhomeapp.shorten.ALIAS_UI_MODEL_STUB_2
import nubank.mobile.nubankhomeapp.shorten.ORIGINAL_URL_STUB_2
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShortenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val resourceProvider: ShortenResourceProvider = mockk(relaxed = true)
    private val repository: ShortenLinkRepository = mockk(relaxed = true)

    private lateinit var viewModel: ShortenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ShortenViewModel(resourceProvider, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `shorten should update state to Loading and then NewAliasCreated on successful shortening`() = runTest {
        // GIVEN
        coEvery { repository.shortenUrl(ORIGINAL_URL_STUB) } returns ALIAS_UI_MODEL_STUB

        // WHEN
        viewModel.shorten(ORIGINAL_URL_STUB)

        // THEN
        val livedataValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { livedataValues.add(it) }
        testDispatcher.scheduler.runCurrent()

        assertThat(livedataValues).hasSize(3)
        assertThat(livedataValues[0]).isEqualTo(State.Empty)
        assertThat(livedataValues[1]).isEqualTo(State.Loading)
        assertThat(livedataValues[2]).isInstanceOf(State.NewAliasCreated::class.java)
        assertThat((livedataValues[2] as State.NewAliasCreated).model).isEqualTo(ALIAS_UI_MODEL_STUB)

        coVerify(exactly = 1) { repository.shortenUrl(ORIGINAL_URL_STUB) }
    }

    @Test
    fun `shorten should update state to InvalidUrlError if URL is invalid`() = runTest {
        // GIVEN
        val invalidUrl = "invalid-url"
        val expectedDialogUIModel = DialogUIModel("Invalid URL", "Please enter a valid URL.", "OK")

        every { resourceProvider.getInvalidUrlDialogModel() } returns expectedDialogUIModel
        val livedataValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { livedataValues.add(it) }

        // WHEN
        viewModel.shorten(invalidUrl)

        // THEN
        testDispatcher.scheduler.runCurrent()

        assertThat(livedataValues).hasSize(2)
        assertThat(livedataValues[0]).isEqualTo(State.Empty)
        assertThat(livedataValues[1]).isInstanceOf(State.InvalidUrlError::class.java)
        assertThat((livedataValues[1] as State.InvalidUrlError).model).isEqualTo(expectedDialogUIModel)

        coVerify(exactly = 0) { repository.shortenUrl(any()) }
        verify(exactly = 1) { resourceProvider.getInvalidUrlDialogModel() }
    }

    @Test
    fun `shorten should update state to GenericError on repository exception`() = runTest {
        // GIVEN
        val expectedDialogUIModel = DialogUIModel("Error", "Something went wrong.", "OK")
        val exception = RuntimeException("API error")

        coEvery { repository.shortenUrl(ORIGINAL_URL_STUB) } throws exception
        every { resourceProvider.getGenericErrorDialogModel() } returns expectedDialogUIModel
        val livedataValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { livedataValues.add(it) }

        // WHEN
        viewModel.shorten(ORIGINAL_URL_STUB)

        // THEN
        testDispatcher.scheduler.runCurrent()

        assertThat(livedataValues).hasSize(3)
        assertThat(livedataValues[0]).isEqualTo(State.Empty)
        assertThat(livedataValues[1]).isEqualTo(State.Loading)
        assertThat(livedataValues[2]).isInstanceOf(State.GenericError::class.java)
        assertThat((livedataValues[2] as State.GenericError).model).isEqualTo(expectedDialogUIModel)

        coVerify(exactly = 1) { repository.shortenUrl(ORIGINAL_URL_STUB) }
        verify(exactly = 1) { resourceProvider.getGenericErrorDialogModel() }
    }

    @Test
    fun `shorten with a second valid URL should add to history and update state`() = runTest {
        // GIVEN
        coEvery { repository.shortenUrl(ORIGINAL_URL_STUB) } returns ALIAS_UI_MODEL_STUB
        coEvery { repository.shortenUrl(ORIGINAL_URL_STUB_2) } returns ALIAS_UI_MODEL_STUB_2

        viewModel.shorten(ORIGINAL_URL_STUB)
        testDispatcher.scheduler.runCurrent()

        val historyAfterFirst = (viewModel.searchBarState.value as State.NewAliasCreated).model

        assertThat(historyAfterFirst).isEqualTo(ALIAS_UI_MODEL_STUB)

        val livedataValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { livedataValues.add(it) }
        testDispatcher.scheduler.runCurrent()

        // WHEN
        viewModel.shorten(ORIGINAL_URL_STUB_2)
        testDispatcher.scheduler.runCurrent()

        // THEN
        assertThat(livedataValues.last()).isInstanceOf(State.NewAliasCreated::class.java)
        val finalHistory = (livedataValues.last() as State.NewAliasCreated).model
        assertThat(finalHistory).isEqualTo(ALIAS_UI_MODEL_STUB_2)

        coVerify(exactly = 1) { repository.shortenUrl(ORIGINAL_URL_STUB) }
        coVerify(exactly = 1) { repository.shortenUrl(ORIGINAL_URL_STUB_2) }
    }

    @Test
    fun `isInvalid should return false for a valid URL`() = runTest {
        // GIVEN
        val validUrl = "https://www.google.com/search?q=test"
        val initialValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { initialValues.add(it) }

        // WHEN
        viewModel.shorten(validUrl)
        testDispatcher.scheduler.runCurrent()

        // THEN
        assertThat(initialValues[1]).isEqualTo(State.Loading)
        assertThat(initialValues.last()).isInstanceOf(State.NewAliasCreated::class.java)
        verify(exactly = 0) { resourceProvider.getInvalidUrlDialogModel() }
    }

    @Test
    fun `alreadyShorten should return false if originalUrl is not in historyList`() = runTest {
        // GIVEN
        coEvery { repository.shortenUrl(ORIGINAL_URL_STUB) } returns ALIAS_UI_MODEL_STUB

        // WHEN
        viewModel.shorten(ORIGINAL_URL_STUB)
        testDispatcher.scheduler.runCurrent()

        // THEN
        val livedataValues = mutableListOf<State>()
        viewModel.searchBarState.observeForever { livedataValues.add(it) }
        testDispatcher.scheduler.runCurrent()

        assertThat(livedataValues.last()).isInstanceOf(State.NewAliasCreated::class.java)
        verify(exactly = 0) { resourceProvider.getAliasAlreadyCreatedDialogModel() }
    }
}
