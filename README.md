# AiPrint
 
Il progetto consiste in una Android App in grado di creare T-shirt personalizzate attraverso l'uso dell'AI,
condividere le creazioni in un social network creato appositamente, acquistare la le T-shirt personalizzare,
gestire il proprio account e visualizzare e interagire con le creazioni di altri utenti.

## SERVIZI ESTERNI UTILIZZATI

### DEEPIMAGE

DeepImage genera delle immagino sfruttando l'intelligenza artificiale a partire da input testuale,
le immagini possono seguire un particolare stile grafico (fantasy, realtà, cartoon) se richiesto.

|  |  |
|-|-|
| Verb | Post |
| BaseUrl | https://api.deepai.org/api/ |
| Path | stable-diffusion |
| Auth | Api-Key Header |
| Body | 'text': 'testo da trasformare in immagine' |
| Header | 'api-key': 'secretApiKey' |
| Response | { "id": "21427652-5c35-465b-90c3-bd39a71395b8", "output_url": https://api.deepai.org/job-view-file/output.jpg" } |

### TEEMILL

Teemill permette di creare uno shop online per il proprio brand mettendo a disposizione le API necessarie per creare una vetrina di vendita totalmente personalizzata

|  |  |
|-|-|
| Verb | Post |
| BaseUrl | https://api.deepai.org/api/](https://teemill.com/omnis/v3 |
| Path | /product/create/ |
| Auth | Bearer Token |
| Body | data={ ‘image_url’: 'url dell’immagine' } |
| Header | Authorization: Bearer SecretKey |
| Response | { "url":"https://demostore.teemill.com/product/api-6yHUvcUkQhXpWV4H8YQP6Naz" } |
| Documentazione | https://teemill.stoplight.io/docs/public-api/29315e4ceff9e-create-product |

## ARCHITETTURA

Il progetto implementa il pattern architetturale Model-View-ViewModel

### MODEL

Le classi nella folder Model rappresentano gli oggetti core dell'applicazione:

* __User__: rappresenta l'utente
* __Image__: rappresenta l'oggetto "immagine di risposta" ritornata dalla chiamata della prima API, dispone solo due proprietà id e image_url
* __ShopImage__: è la classe che rappresenta la risposta della seconda API
* __Post__: è il core entity dell'applicazione, viene utilizzato per la creazione dei post degli utenti, salvato sia su Firebase che in locale con Room
* __Like__: rappresenta il "mi piace" di un utente ad un post, viene salvato poi su Firebase in una collection dedicata

### VIEWMODEL

Di seguito si riporta un esempio di ViewModel ma il discorso è speculare per quasi tutti i ViewModel:

ImageViewModel: la classe estende “ViewModel” e fa da intermediario tra AiPrintGeneratorFragment(view) e ImageRepositoryWithLiveData(repository).

Implementa il metodo GetAiImage(string imageDescription) che ritorna un MutableLiveData<Result>.
Tale metodo è richiamato dalla view e in particolare resta “in ascolto” con l’observer su eventuali cambi di stato dell’oggetto
MutableLiveData<Result> per aggiornare la UI.

L’ImageViewModel chiama il corrispettivo metodo GetAiImageUrl(string imageDescription) presente in ImageRepositoryWithLiveData
che a sua volta invoca il metodo la classe BaseRemoteDataSource (o meglio, una delle sue implementazioni concrete)
per interagire effettivamente con le API o con il Database.

### REPOSITORY

Di seguito si riporta un esempio di Repository ma il discorso è speculare per quasi tutti i Repository. 

ImageRepositoryWithLiveData: implementa l’interfaccia IImageRepositoryWithLiveData e IImageCallback,
di conseguenza implementa i metodi:

* GetAiImageUrl(string imageDescription): invoca il metodo della classe ImageRemoteDataSource per chiamare effettivamente l’API.
* Metodi di callback onSuccessFromRemote e onFailureFromRemote per aggiornare il MutableLiveData o gestire l’errore.

### CONCETTI TECNICI CHIAVE

__Service__: sono state implementate le interfacce Service, utilizzare per effettuare le chiamate alle API esterne con la libreria Retrofit.

__Singleton__: la classe ServiceLocator, che utilizza il Builder di Retrofit per istanziare le varie classi Service, implementa il pattern Singleton.
Questo significa che della classe esisterà solo e soltanto un’istanza per tutta la durata dell’esecuzione dell’applicazione.

__Repository__: sono state implementate le rispettive classi Repository, utilizzate dai ViewModel per recuperare le informazioni da far vedere nelle view.

__Adapter__: sono stati implementati gli adapter necessari alle RecyclerView per associare correttamente i dati alle relative visualizzazioni,
hanno anche la funzione di segnalare gli eventi come il clic sugli elementi della lista per generare eventi

__Classi DataSource__: hanno il compito di interagire con le API esterne e con il database, locale o remoto che sia, per il recupero delle informazioni.
Grazie all’astrazione delle interfacce è possibile decidere nel ServiceLocator oppure a runtime,
se recuperare i dati utilizzando le API (o database) oppure in locale per facilitare, ad esempio, la parte di testing.

__Persistenza con Room__: la libreria Room rappresenta un abstraction layer, uno strato software che permette di sfruttare tutte le potenzialità del database.

### RECYCLERVIEW

Per quanto riguarda la visualizzazione dei post viene implementata una lista dinamica attraverso l’utilizzo di una RecyclerView.
Quest’ultima, dopo aver definito l’aspetto di ciascun elemento e averle fornito i dati,
semplifica la visualizzazione efficiente di grandi set di dati creando dinamicamente gli elementi quando sono necessari,
questi singoli elementi vengono riciclati quando escono fuori dallo schermo consentendo un miglioramento generale delle prestazioni e una riduzione del consumo energetico.

Come gestore del layout ne abbiamo utilizzati due tipi:

* __LinearLayoutManager__ per la visualizzazione lineare degli elementi della lista nella sezione “Home” dell’applicazione
* __GridLayoutManager__ per la visualizzazione a griglia degli elementi della lista nella sezione “Favourite” dell’applicazione

Per interfacciare la RecyclerView con il set di dati è presente un apposito Adapter che associa i dati alle visualizzazioni all’interno di un oggetto RecyclerView,
inoltre l’adapter ha anche la funzione di segnalare gli eventi di clic sugli elementi

## FLUSSO DI UNA CHIAMATA API

Caso d’uso: Generazione di un’immagine chiamando l’api di DeepImage a partire da una descrizione testuale fornita in input dall’utente.

Il caso d’uso inizia quando l’utente accede alla sezione Generate, presente nel bottom navigation menu.
La classe AiPrintGeneratorFragment sarà la responsabile dell’interfaccia grafica e di tutto ciò che ne segue (es: aggiornamento dinamico della UI)

Nella vista gli elementi principali sono:

* EditText: dove l’utente descriverà con una parola/frase l’immagine che vorrà far creare dall’AI.
* Button: per iniziare il processo di generazione dell’immagine e dello shop.

A seguito del click del bottone verrà invocato il SetOnClickListner() associato.
Al suo interno il fragment chiamerà il metodo GetAiImage(string inputText) implementato nella classe ImageViewModel.
Il metodo ritorna un MutableLiveData<Result> dunque in fragment avrà bisogno di un observer su tale oggetto per aggiornare la UI quando l’oggetto mutable cambia di stato.

A tal proposito il metodo GetAiImage(string inputText) ritorna l’oggetto MutableLiveData.
Quando la chiamata API è ultimata, viene aggiornato lo stato dell’oggetto che in automatico triggera l’observer per, ad esempio, aggiornare la vista.

Il metodo GetAiImage(string inputText) chiamerà il metodo GetAiImageUrl(string url) definito dall’interfaccia IImageRepositoryWithLiveData
e implementato in ImageRepositoryWithLiveData.

Il repository ha il compito di “invocare” il recupero dei dati dalle classi di DataSource e di aggiornare l’oggetto MutableLiveData<Result>
quando l’operazione di recupero è ultimata. In particolare quest’ultimo passaggio avviene nel metodo OnSuccessFromRemote(ImageApiResponse)
definito nell’interfaccia IImageCallback ed invocato nelle classi di DataSource quando il recupero è andato a buon fine.

Le classi DataSource hanno lo scopo di recuperare le informazioni effettive degli utenti, post, shop e like dal database o dalle api esterne.
Per rendere il codice modulare e facilmente testabile, si utilizza la classe astratta BaseImageRemoteDataSource
che definisce i metodi che le classi concrete dovranno implementare (totalmente o parzialmente). Le classi concrete di interesse, in questo caso,
sono in ImageRemoteDataSource e ImageMockRemoteDataSource.

* La classe ImageRemoteDataSource implementa i metodi invocando l’api di DeepImage per creare l’immagine, che viene ritornata come url.
* La classe ImageMockRemoteDataSource invece implementa i metodi prendendo le informazioni da un file json locale dello stesso formato di risposta dell’API di
  DeepImage effettiva, simulando dunque la chiamata. Questa è utile in particolare in fase di testing per evitare innumerevoli chiamate al servizio esterno.

Come accennato in precedenza, entrambe le classi chiameranno i metodi di callback sia in caso di successo che di insuccesso,
definiti dall’interfaccia IImageCallback, e implementati nella classe di Repository.

Una volta aggiornato il valore dell’oggetto MutableLiveData<Result> l’observer relativo aggiornerà la UI.
<div align=center>
 <img width="900" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/ee792356-c0dc-40e1-b234-b5ae34637243">
</div>

## DATABASE

### SALVATAGGIO SUL DATABASE REMOTO: FIREBASE

Il database principale per salvare i dati di AiPrint è Firebase.

I dati vengono archiviati come JSON e sincronizzati in tempo reale con ogni client connesso. In particolare, vengono utilizzati due servizi offerti da Firebase:

* Firebase __Authentication__, necessario per gestire il login e registrazione dell’utente tramite le credenziali di accesso quali email e password.
* Firebase RealTime DB, nel quale abbiamo strutturato tre collection
○ User: Contiene le informazioni degli utenti come email e nome utente.
○ Post: Salva le informazioni dei post creati dagli utenti.
○ Like: Contiene le interazioni (‘mi piace’) tra utente e post. Nella collection
sono inseriti tutti i mi piace che un utente ha dato ad un post in una relazione
N-M












