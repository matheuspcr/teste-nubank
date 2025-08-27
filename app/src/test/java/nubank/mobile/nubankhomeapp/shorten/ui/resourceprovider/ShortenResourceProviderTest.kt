package nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel
import nubank.mobile.nubankhomeapp.utils.ResourceProvider
import org.junit.Before
import org.junit.Test

internal class ShortenResourceProviderTest {

    private val resourceProvider: ResourceProvider = mockk()
    private lateinit var shortenResourceProvider: ShortenResourceProvider

    @Before
    fun setup() {
        shortenResourceProvider = ShortenResourceProviderImpl(resourceProvider)
    }

    @Test
    fun `getInvalidUrlDialogModel should return correct DialogUIModel`() {
        // GIVEN
        val expectedTitle = "URL Inválida"
        val expectedMessage = "Por favor, insira uma URL válida."
        val expectedButtonText = "OK"

        every { resourceProvider.getString(R.string.invalid_url_dialog_title) } returns expectedTitle
        every { resourceProvider.getString(R.string.invalid_url_dialog_message) } returns expectedMessage
        every { resourceProvider.getString(R.string.dialog_button) } returns expectedButtonText

        // WHEN
        val result = shortenResourceProvider.getInvalidUrlDialogModel()

        // THEN
        assertThat(result).isInstanceOf(DialogUIModel::class.java)
        assertThat(result.title).isEqualTo(expectedTitle)
        assertThat(result.message).isEqualTo(expectedMessage)
        assertThat(result.buttonText).isEqualTo(expectedButtonText)
    }

    @Test
    fun `getGenericErrorDialogModel should return correct DialogUIModel`() {
        // GIVEN
        val expectedTitle = "Erro!"
        val expectedMessage = "Ocorreu um erro inesperado. Tente novamente mais tarde."
        val expectedButtonText = "OK"

        every { resourceProvider.getString(R.string.generic_error_dialog_title) } returns expectedTitle
        every { resourceProvider.getString(R.string.generic_error_dialog_message) } returns expectedMessage
        every { resourceProvider.getString(R.string.dialog_button) } returns expectedButtonText

        // WHEN
        val result = shortenResourceProvider.getGenericErrorDialogModel()

        // THEN
        assertThat(result).isInstanceOf(DialogUIModel::class.java)
        assertThat(result.title).isEqualTo(expectedTitle)
        assertThat(result.message).isEqualTo(expectedMessage)
        assertThat(result.buttonText).isEqualTo(expectedButtonText)
    }

    @Test
    fun `getAliasAlreadyCreatedDialogModel should return correct DialogUIModel with formatted message`() {
        // GIVEN
        val position = 3
        val expectedMessage = "Este link já foi encurtado e está na posição 3 da sua lista."
        val expectedButtonText = "Entendi"

        every { resourceProvider.getString(R.string.alias_already_created_dialog_message, position) } returns expectedMessage
        every { resourceProvider.getString(R.string.dialog_button) } returns expectedButtonText

        // WHEN
        val result = shortenResourceProvider.getAliasAlreadyCreatedDialogModel(position)

        // THEN
        assertThat(result).isInstanceOf(DialogUIModel::class.java)
        assertThat(result.title).isNull()
        assertThat(result.message).isEqualTo(expectedMessage)
        assertThat(result.buttonText).isEqualTo(expectedButtonText)
    }
}