package nubank.mobile.nubankhomeapp.shorten.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.databinding.ActivityShortenBinding
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel
import nubank.mobile.nubankhomeapp.shorten.ui.model.DialogUIModel
import nubank.mobile.nubankhomeapp.shorten.ui.shortenedList.ShortenedListActivity
import nubank.mobile.nubankhomeapp.shorten.ui.viewmodel.ShortenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import nubank.mobile.nubankhomeapp.shorten.ui.model.SearchBarState as State

class ShortenActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    private val viewModel by viewModel<ShortenViewModel>()
    private val binding by lazy { ActivityShortenBinding.inflate(layoutInflater) }

    // Esse comentário serve para disparar o workflow do github actions que executa os testes unitários do projeto.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupEdgeInsets()
        setupListener()
        setupObservers()
    }

    private fun setupEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListener() = with(binding) {
        searchButton.setOnClickListener {
            viewModel.shorten(searchBarEditText.text.toString())
        }
        checkRecentlyUrlsButton.setOnClickListener {
            startActivity(Intent(this@ShortenActivity, ShortenedListActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.searchBarState.observe(this, ::handleSearchBarState)
    }

    private fun handleSearchBarState(state: State) {
        binding.searchBarProgress.isVisible = state is State.Loading

        when(state) {
            is State.NewAliasCreated -> setupLastAlias(state.model)
            is State.InvalidUrlError -> showDialog(state.model)
            is State.GenericError -> showDialog(state.model)
            is State.AliasAlreadyCreated -> showDialog(state.model)
            else -> Unit
        }
    }

    private fun setupLastAlias(model: AliasLinksUIModel) = with(binding) {
        aliasText.text = model.alias
        shortenUrlText.text = model.shortenUrl
        originalUrlText.text = model.originalUrl
    }

    private fun showDialog(model: DialogUIModel) {
        AlertDialog.Builder(this)
            .setTitle(model.title)
            .setMessage(model.message)
            .setNeutralButton(model.buttonText, this)
            .show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.searchBarState.removeObservers(this)
    }
}
