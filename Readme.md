# Projeto Encurtador de Links Nubank Home App

Este projeto é um aplicativo Android simples que permite aos usuários encurtar URLs e visualizar um histórico dos links criados. Ele serve como um exemplo prático de aplicação de conceitos modernos de desenvolvimento Android.

## 1. Arquitetura do Projeto

O projeto segue princípios de **MVVM**, dividindo o código em camadas distintas de view e model, e realizando a comunicação através da ViewModel, para garantir separação de preocupações, testabilidade e manutenibilidade.

A estrutura de pacotes reflete essa separação:

![Arquitetura](/docs/arquitetura.png)

### Detalhamento das Camadas

* **Camada de Dados (`DATA`)**:
    * **`service` (API)**: Define a interface da API remota usando Retrofit para comunicação com o serviço de encurtamento de URLs.
    * **`model`**: Contém as classes de dados que representam as requisições e respostas da API.
    * **`converter`**: Responsável por transformar os modelos de dados da API em modelos mais adequados para a camada de UI (`AliasLinksUIModel`). Isso garante que a UI não dependa diretamente dos modelos da API.
    * **`repository` (Implementação)**: `ShortenLinkRepositoryImpl` implementa a interface `ShortenLinkRepository`. Ele orquestra a chamada à API através do `AliasApiService` e usa o `AliasModelConverter` para adaptar a resposta para a camada de UI. Ele encapsula a fonte de dados, escondendo a complexidade da rede da camada de apresentação.


* **Camada de Apresentação (`UI`)**:
    * **`model`**: Define `DialogUIModel` e `AliasLinksUIModel` que são usados exclusivamente na UI para representar o estado da interface e os dados a serem exibidos. `SearchBarState` define os diferentes estados da barra de pesquisa e da UI.
    * **`resourceprovider`**: `ShortenResourceProvider` e sua implementação `ShortenResourceProviderImpl` abstraem o acesso a recursos do Android (como strings) para facilitar a testabilidade do `ViewModel` sem depender diretamente do contexto Android.
    * **`viewmodel`**: `ShortenViewModel` é o `ViewModel` (padrão MVVM) que contém a lógica de negócios da UI. Ele interage com o `ShortenLinkRepository` para encurtar URLs e com o `ShortenResourceProvider` para obter mensagens da UI. Ele expõe um `LiveData` (`searchBarState`) para a `Activity` observar as mudanças de estado da UI (carregamento, erro, sucesso, etc.).
    * **`ShortenActivity`**: É a `Activity` principal que observa o `searchBarState` do `ViewModel` e atualiza a UI de acordo (mostra progresso, exibe lista, mostra dialogs de erro). Ela é responsável por interações de UI, como cliques em botões e exibição de diálogos.
    * **`list`**: Contém o `ShortenListAdapter` e os `ViewHolder`s para exibir a lista de URLs encurtadas no `RecyclerView`.


* **Camada de Injeção de Dependência (`DI`)**:
    * **`MainModule.kt`**: Utiliza a biblioteca Koin para definir e gerenciar as dependências de todo o aplicativo. Ele configura como as instâncias das classes (como `Retrofit`, `OkHttpClient`, `AliasApiService`, `ShortenLinkRepository`, `ShortenResourceProvider` e `ShortenViewModel`) são fornecidas e injetadas.

## 2. Bibliotecas Utilizadas

* **Koin**: Uma biblioteca de injeção de dependência leve para Kotlin. Usado para gerenciar e fornecer as dependências do projeto, como `ViewModel`, `Repository` e `Service`.
* **Retrofit**: Um cliente HTTP type-safe para Android e Java. Usado para fazer requisições à API de encurtamento de URLs.
* **OkHttp (com HttpLoggingInterceptor)**: Cliente HTTP subjacente ao Retrofit. O `HttpLoggingInterceptor` é usado para logar as requisições e respostas HTTP, útil para depuração.
* **Gson (com GsonConverterFactory)**: Uma biblioteca de serialização/desserialização JSON usada pelo Retrofit para converter objetos Kotlin em JSON e vice-versa.
* **Kotlin Coroutines**: Para programação assíncrona, especialmente para operações de rede e gerenciamento de threads no `ViewModel`.
* **AndroidX Lifecycle & LiveData**: Componentes do Android Jetpack para gerenciar o ciclo de vida e observar dados reativos (`LiveData`) no `ViewModel` e na `Activity`.
* **AndroidX Activity, AppCompat, Core-KTX**: Componentes básicos do Android para atividades, compatibilidade e extensões Kotlin.
* **Material Design Components**: Para componentes de UI modernos do Android, como `TextInputLayout`, `TextInputEditText`, `LinearProgressIndicator`, `MaterialDivider`.
* **AndroidX RecyclerView**: Para exibir listas de forma eficiente.
* **AndroidX ConstraintLayout**: Para construir layouts flexíveis e otimizados.
* **MockK**: Uma biblioteca de mocking para Kotlin, utilizada nos testes unitários e instrumentados para criar duplos de teste e verificar interações.
* **JUnit 4**: Framework de teste unitário padrão para Java/Kotlin.
* **Truth (Google)**: Biblioteca de asserções que torna os testes mais legíveis e expressivos.
* **AndroidX Test (espresso-core, ext-junit, rules, runner, core)**: Ferramentas e bibliotecas para testes instrumentados em Android, incluindo o Espresso para interação com a UI.
* **AndroidX Arch Core Testing (InstantTaskExecutorRule)**: Uma regra de JUnit para testes de LiveData, garantindo que as operações de background sejam executadas sincronicamente.

## 3. Funcionalidades Presentes

* **Encurtar URLs**: O usuário pode digitar uma URL em um campo de texto e clicar em um botão para encurtá-la.
* **Histórico de URLs**: Os links encurtados com sucesso são adicionados a uma lista abaixo do campo de entrada, mostrando tanto a URL original quanto a encurtada.
* **Validação de URL**: O aplicativo verifica se a URL inserida tem um formato válido. Se for inválida, um diálogo de erro é exibido.
* **Detecção de Duplicidade**: Se o usuário tentar encurtar uma URL que já está no histórico, um diálogo informará que a URL já foi encurtada e indicará sua posição na lista.
* **Tratamento de Erros Genéricos**: Em caso de falha na comunicação com a API ou outros erros inesperados, um diálogo de erro genérico é mostrado ao usuário.

## 4. Evidencias Testes Unitários

![Unitários](/docs/unitarios.png)

## 5. Evidencias Testes Instrumentados

Dispositivo Pixel 2 API 34 sem Play Store

![Instrumentados](/docs/instrumentados.png)

![Video Instrumentados](/docs/video_instrumentados.gif)
