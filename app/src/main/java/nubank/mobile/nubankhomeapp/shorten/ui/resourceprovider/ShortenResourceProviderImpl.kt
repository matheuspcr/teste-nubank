package nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider

import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel
import nubank.mobile.nubankhomeapp.utils.ResourceProvider

internal class ShortenResourceProviderImpl(private val provider: ResourceProvider) :
    ShortenResourceProvider {

    override fun getInvalidUrlDialogModel() = DialogUIModel(
        title = provider.getString(R.string.invalid_url_dialog_title),
        message = provider.getString(R.string.invalid_url_dialog_message),
        buttonText = provider.getString(R.string.dialog_button)
    )

    override fun getGenericErrorDialogModel() = DialogUIModel(
        title = provider.getString(R.string.generic_error_dialog_title),
        message = provider.getString(R.string.generic_error_dialog_message),
        buttonText = provider.getString(R.string.dialog_button)
    )

    override fun getAliasAlreadyCreatedDialogModel() = DialogUIModel(
        title = null,
        message = provider.getString(R.string.alias_already_created_dialog_message),
        buttonText = provider.getString(R.string.dialog_button)
    )
}
