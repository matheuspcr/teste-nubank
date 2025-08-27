package nubank.mobile.nubankhomeapp.di

import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProvider
import nubank.mobile.nubankhomeapp.shorten.ui.resourceprovider.ShortenResourceProviderImpl
import nubank.mobile.nubankhomeapp.shorten.data.service.AliasApiService
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepositoryImpl
import nubank.mobile.nubankhomeapp.shorten.data.converter.AliasModelConverter
import nubank.mobile.nubankhomeapp.shorten.data.converter.AliasModelConverterImpl
import nubank.mobile.nubankhomeapp.shorten.data.datasource.ShortenLinkDataSourceI
import nubank.mobile.nubankhomeapp.shorten.data.datasource.ShortenLinkInMemoryDataSource
import nubank.mobile.nubankhomeapp.shorten.ui.shortenedList.ShortenedListViewModel
import nubank.mobile.nubankhomeapp.shorten.ui.viewmodel.ShortenViewModel
import nubank.mobile.nubankhomeapp.utils.ResourceProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val mainModule = module {
    factory<Retrofit> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("https://url-shortener-server.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .callTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
    factory {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
    factory<AliasApiService> {
        get<Retrofit>().create(AliasApiService::class.java)
    }
    factory<AliasModelConverter> { AliasModelConverterImpl() }
    factory<ShortenLinkRepository> {
        ShortenLinkRepositoryImpl(
            service = get(),
            converter = get(),
            dataSource = get()
        )
    }
    factory { ResourceProvider(context = androidApplication()) }
    factory<ShortenResourceProvider> { ShortenResourceProviderImpl(provider = get()) }
    viewModel { ShortenViewModel(shortenRepository = get(), provider = get()) }
    viewModel { ShortenedListViewModel(shortenRepository = get()) }
    single<ShortenLinkDataSourceI> { ShortenLinkInMemoryDataSource() }
}