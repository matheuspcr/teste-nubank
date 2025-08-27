package nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider

import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel

interface ShortenResourceProvider {
    fun getInvalidUrlDialogModel() : DialogUIModel

    fun getGenericErrorDialogModel() : DialogUIModel

    fun getAliasAlreadyCreatedDialogModel() : DialogUIModel
}
