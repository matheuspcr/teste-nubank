package nubank.mobile.nubankhomeapp.shorten.ui.model

sealed interface SearchBarState {
    data object Empty: SearchBarState
    data object Loading: SearchBarState
    data class NewAliasCreated(val model: AliasLinksUIModel): SearchBarState
    data class GenericError(val model: DialogUIModel): SearchBarState
    data class InvalidUrlError(val model: DialogUIModel): SearchBarState
    data class AliasAlreadyCreated(val model: DialogUIModel): SearchBarState
}