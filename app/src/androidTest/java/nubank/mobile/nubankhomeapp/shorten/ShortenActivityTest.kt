package nubank.mobile.nubankhomeapp.shorten

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.helper.Matcher.atPosition
import nubank.mobile.nubankhomeapp.helper.waitFor
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.mocks.ResourceProviderMock
import nubank.mobile.nubankhomeapp.shorten.mocks.ShortenLinkRepositoryMock
import nubank.mobile.nubankhomeapp.shorten.ui.ShortenActivity
import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProvider
import nubank.mobile.nubankhomeapp.shorten.ui.viewmodel.ShortenViewModel
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB_2
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB_3
import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
internal class ShortenActivityTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(ShortenActivity::class.java)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context get() = ApplicationProvider.getApplicationContext<Context>()
    private val repository: ShortenLinkRepository = ShortenLinkRepositoryMock()
    private val resourceProvider: ShortenResourceProvider = ResourceProviderMock(context)

    @Before
    fun setup() {
        GlobalContext.stopKoin()
        startKoin {
            androidContext(context)
            modules(
                module {
                    factory { repository }
                    factory { resourceProvider }
                    viewModel { ShortenViewModel(get(), get()) }
                }
            )
        }
    }

    @Test
    fun givenActivityStartsWhenInitialStateThenComponentsAreVisibleAndProgressIsHidden() {
        // THEN
        Espresso.onView(withId(R.id.search_bar)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.search_bar_edit_text)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.search_button)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.list_header)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.recently_urls_list)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.search_bar_progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun givenValidURLWhenShortenButtonClickedThenURLIsShortenedAndAddedToList() {
        // WHEN
        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(typeText(ORIGINAL_URL_STUB))
        Espresso.onView(withId(R.id.search_button)).perform(click())

        // THEN
        Espresso.onView(withId(R.id.recently_urls_list))
            .check(matches(hasDescendant(withText(ORIGINAL_URL_STUB))))
    }

    @Test
    fun givenInvalidURLWhenShortenButtonClickedThenInvalidUrlErrorDialogIsShown() {
        // GIVEN
        val invalidUrl = "invalid"
        val expectedTitle = context.getString(R.string.invalid_url_dialog_title)
        val expectedMessage = context.getString(R.string.invalid_url_dialog_message)
        val expectedButtonText = context.getString(R.string.dialog_button)

        // WHEN
        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(typeText(invalidUrl))
        Espresso.onView(withId(R.id.search_button)).perform(click())

        // THEN
        Espresso.onView(withText(expectedTitle)).check(matches(isDisplayed()))
        Espresso.onView(withText(expectedMessage)).check(matches(isDisplayed()))
        Espresso.onView(withText(expectedButtonText)).check(matches(isDisplayed()))

        Espresso.onView(withText(expectedButtonText)).perform(click())
        Espresso.onView(withText(expectedTitle)).check(doesNotExist())
    }

    @Test
    fun givenValidURLWhenShortenButtonClickedAndRepositoryThrowsErrorThenGenericErrorDialogIsShown() {
        // GIVEN
        val errorUrl = "https://teste.com/error"
        val expectedTitle = context.getString(R.string.generic_error_dialog_title)
        val expectedMessage = context.getString(R.string.generic_error_dialog_message)
        val expectedButtonText = context.getString(R.string.dialog_button)

        // WHEN
        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(typeText(errorUrl))
        Espresso.onView(withId(R.id.search_button)).perform(click())

        // THEN
        waitFor()
        Espresso.onView(withText(expectedTitle)).check(matches(isDisplayed()))
        Espresso.onView(withText(expectedMessage)).check(matches(isDisplayed()))
        Espresso.onView(withText(expectedButtonText)).check(matches(isDisplayed()))

        Espresso.onView(withText(expectedButtonText)).perform(click())
        Espresso.onView(withText(expectedTitle)).check(doesNotExist())
    }

    @Test
    fun givenURLAlreadyShortenedWhenShortenButtonClickedThenAliasAlreadyCreatedDialogIsShown() {
        // GIVEN
        val expectedButtonText = context.getString(R.string.dialog_button)

        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(typeText(ORIGINAL_URL_STUB))
        Espresso.onView(withId(R.id.search_button)).perform(click())

        // WHEN
        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(clearText(), typeText(ORIGINAL_URL_STUB))
        Espresso.onView(withId(R.id.search_button)).perform(click())

        // THEN
        Espresso.onView(withText(containsString("Your position on the list is 1"))).check(matches(isDisplayed()))
        Espresso.onView(withText(expectedButtonText)).check(matches(isDisplayed()))

        Espresso.onView(withText(expectedButtonText)).perform(click())
        Espresso.onView(withText(containsString("Your position on the list is 1"))).check(doesNotExist())
    }

    @Test
    fun givenMultipleURLsShortenedThenAllAreDisplayedInTheListInOrder() {
        // WHEN
        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(typeText(ORIGINAL_URL_STUB))
        Espresso.onView(withId(R.id.search_button)).perform(click())
        waitFor()
        Espresso.onView(withId(R.id.search_bar_progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(clearText(), typeText(ORIGINAL_URL_STUB_2))
        Espresso.onView(withId(R.id.search_button)).perform(click())
        waitFor()
        Espresso.onView(withId(R.id.search_bar_progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(clearText(), typeText(ORIGINAL_URL_STUB_3))
        Espresso.onView(withId(R.id.search_button)).perform(click())
        waitFor()
        Espresso.onView(withId(R.id.search_bar_progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        Espresso.onView(withId(R.id.search_bar_edit_text)).perform(closeSoftKeyboard())

        // THEN
        Espresso.onView(withId(R.id.recently_urls_list))
            .check(matches(hasDescendant(withText(ORIGINAL_URL_STUB))))
            .check(matches(hasDescendant(withText(ORIGINAL_URL_STUB_2))))
            .check(matches(hasDescendant(withText(ORIGINAL_URL_STUB_3))))

        Espresso.onView(withId(R.id.recently_urls_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(atPosition(0, hasDescendant(withText(ORIGINAL_URL_STUB)))))

        Espresso.onView(withId(R.id.recently_urls_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
            .check(matches(atPosition(1, hasDescendant(withText(ORIGINAL_URL_STUB_2)))))

        Espresso.onView(withId(R.id.recently_urls_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
            .check(matches(atPosition(2, hasDescendant(withText(ORIGINAL_URL_STUB_3)))))
    }
}