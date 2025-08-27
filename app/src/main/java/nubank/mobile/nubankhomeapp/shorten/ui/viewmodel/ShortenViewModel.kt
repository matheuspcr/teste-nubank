package nubank.mobile.nubankhomeapp.shorten.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProvider
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel
import nubank.mobile.nubankhomeapp.shorten.ui.list.ShortenListAdapter
import nubank.mobile.nubankhomeapp.shorten.ui.model.SearchBarState as State

internal class ShortenViewModel(
    private val provider: ShortenResourceProvider,
    private val shortenRepository: ShortenLinkRepository
) : ViewModel() {
    private val _searchBarState = MutableLiveData<State>(State.Empty)
    val searchBarState: LiveData<State> = _searchBarState

    private var lastAlias: AliasLinksUIModel? = null

    fun shorten(url: String) {
        if (url.isInvalid()) return
        if (url.alreadyShorten()) return

        viewModelScope.launch {
            _searchBarState.value = State.Loading
            try {
                val alias = shortenRepository.shortenUrl(url)
                lastAlias = alias
                _searchBarState.value = State.NewAliasCreated(alias)
            } catch (e: Exception) {
                _searchBarState.value = State.GenericError(provider.getGenericErrorDialogModel())
            }
        }
    }

    private fun String.isInvalid(): Boolean {
        val isValid = URL_PATTERN.toRegex().matches(this)
        if (!isValid) {
            _searchBarState.value = State.InvalidUrlError(provider.getInvalidUrlDialogModel())
        }
        return !isValid
    }

    private fun String.alreadyShorten(): Boolean {
        val alreadyCreated = shortenRepository.checkAlreadyCreated(this)
        if (alreadyCreated) {
            _searchBarState.value = State.AliasAlreadyCreated(provider.getAliasAlreadyCreatedDialogModel())
        }
        return alreadyCreated
    }

    private companion object {
        const val URL_PATTERN = "^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*$"
    }
}