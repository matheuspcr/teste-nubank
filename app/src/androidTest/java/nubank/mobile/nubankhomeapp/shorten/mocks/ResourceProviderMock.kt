package nubank.mobile.nubankhomeapp.shorten.mocks

import android.content.Context
import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel
import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProvider

internal class ResourceProviderMock(private val context: Context): ShortenResourceProvider {
    override fun getInvalidUrlDialogModel(): DialogUIModel {
        return DialogUIModel(
            context.getString(R.string.invalid_url_dialog_title),
            context.getString(R.string.invalid_url_dialog_message),
            context.getString(R.string.dialog_button)
        )
    }

    override fun getGenericErrorDialogModel(): DialogUIModel {
        return DialogUIModel(
            context.getString(R.string.generic_error_dialog_title),
            context.getString(R.string.generic_error_dialog_message),
            context.getString(R.string.dialog_button)
        )
    }

    override fun getAliasAlreadyCreatedDialogModel(position: Int): DialogUIModel {
        return DialogUIModel(
            null,
            context.getString(R.string.alias_already_created_dialog_message, position),
            context.getString(R.string.dialog_button)
        )
    }
}