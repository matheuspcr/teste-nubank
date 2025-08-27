package nubank.mobile.nubankhomeapp.shorten.ui.shortenedList

import androidx.lifecycle.ViewModel
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class ShortenedListViewModel(
    private val shortenRepository: ShortenLinkRepository
) : ViewModel() {

    fun getList(): List<AliasLinksUIModel> {
        return shortenRepository.getAliasList()
    }

    fun deleteAlias(position: Int) {
        shortenRepository.deleteAlias(position)
    }
}