# AiPrint
 
Il progetto consiste in una Android App in grado di creare T-shirt personalizzate attraverso l'uso dell'AI,
condividere le creazioni in un social network creato appositamente, acquistare la le T-shirt personalizzare,
gestire il proprio account e visualizzare e interagire con le creazioni di altri utenti.

### TECNOLOGIE IMPLEMENTATE

Il progetto è stato sviluppato utilizzando le seguenti tecnologie: Java, SQL, Firebase, Room, API (DeepImage, Teemill), 

Inoltre segue i pattern architetturali: MVC, Service, Singleton, Repository, Adapter

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
* Firebase __RealTime DB__, nel quale sono strutturate tre collection:
  - User: Contiene le informazioni degli utenti come email e nome utente.
  - Post: Salva le informazioni dei post creati dagli utenti.
  - Like: Contiene le interazioni (‘mi piace’) tra utente e post. Nella collection sono inseriti tutti i mi piace che un utente ha dato ad un post in una relazione N-M

### SALVATAGGIO SUL DATABASE LOCALE: ROOM

I post alla quale l'utente mette like vengono salvati nella memoria del proprio dispositivo attraverso il database Room (parte della suite Android Jetpack)

L’applicazione mette a disposizione dell’utente la possibilità di salvare i post per poi consultarli in seguito, anche in modalità offline.
In particolare per sviluppare la funzionalità le classi principali utilizzate sono:

* __PostDao__: è un'interfaccia che racchiude i metodi e le relative query per l’accesso ai dati del database locale
  (ad esempio il metodo getAllPost() viene preceduto dal decorator Query con “SELECT * FROM post ORDER BY date DESC”)
* __PostRoomDatabase__ classe astratta che rappresenta il database Room con entità Post, contiene i riferimenti a PostDao, l’istanza, il numero di thread,
  l’ExecutorService e il metodo pubblico getDatabase con il quale è possibile ricevere l’istanza del database (se l’istanza è nulla viene creata con databaseBuilder)

Le classi citate sono utilizzate dal Post Local DataSource, che si inserisce nel contesto più ampio dell’architettura viewmodel descritta in precedenza.

### LOGIN E REGISTRAZIONE

La parte relativa agli account è gestita tramite Firebase Authentication (in alternativa è possibile usare un account Google per accedere)

Attualmente la registrazione ad AiPrint è obbligatoria, pertanto la schermata principale al primo avvio dell’applicazione è la registrazione o la login (Welcome Activity).

Una volta che l’utente effettua la login, i suoi dati vengono crittografati e salvati in locale con lo scopo di evitare l’autenticazione ad ogni avvio dell’app.
A questo punto viene lanciata la MainActivity.

<div align=center>
 <img width="900" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/1c602596-b5f9-4686-8bfe-40d0800e0294">
</div>

## DESIGN

Il deisgn dell'applicazione è stato sviluppato seguendo le linee guida generali del material design 3 con qualche aggiunta e personalizzazione.

__WelcomeFragment__: Pagina di benvenuto ai nuovi utenti, offre la possibilità di fare il login o la registrazione (la navigazione è gestita tramite navigation graph)

<div align=center>
 <img width="200" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/436f0917-619a-4d05-8d88-740d41d79783">
</div>

__LoginFragment__: Interfaccia per effettuare la Login, se l’utente è già registrato, altrimenti apparirà un messaggio di errore specifico al contesto.
I campi presenti sono due EditText rispettivamente per l’inserimento dell’email e della password.
Al click del bottone “Login” verrà interrogato Firebase per verificare l’autenticità delle credenziali e, in caso di successo, ritornare il tokenId dell’utente.

<div align=center>
 <img width="200" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/33bf7193-6e9a-402f-9d7f-d2b237399960">
</div>

__RegistrationFragment__: Interfaccia per effettuare la registrazione dell’utente nel se non è già registrato,
in caso contrario apparirà un messaggio di errore specifico al contesto. I campi presenti sono quattro EditText rispettivamente per l’inserimento dello username,
dell’email e della password e un datepicker per la data di nascita. Inoltre, è disponibile la registrazione con il proprio account Google.
Al click del bottone “Registrati” verranno validate le email (che sia nel formato corretto) e la password
(lunghezza minima, obbligo di caratteri speciali e lettere maiuscole). Se tutto conforme l’utente verrà inserito in firebase.

<div align=center>
 <img width="200" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/974a6130-af07-4be7-acb1-75db62a30b75">
</div>

__HomeFragment__: Nell’interfaccia è presente una RecycleView che contiene gli ultimi post generati dagli utenti. In particolare per ogni post l’utente può:

* Mettere like, quindi salvarlo nei preferiti
* Cliccare sul carrello per visualizzare lo shop relativo al post ed eventualmente comprare l’articolo
* Salvarlo in locale per poterlo vedere anche offline

<div align=center>
 <img width="200" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/35a31e76-9af3-45c8-94d2-4883e3890643">
</div>

__FavouritePostFragment__: Questa sezione ha il compito di mostrare i post a cui l’utente ha messo like e quelli salvati in memoria locale.
Nella sezione alta della view è possibile scegliere quali delle due sezioni visualizzare. Nel caso si volessero visualizzare i post salvati,
questi verranno letti dal database in locale. I post preferiti invece verranno letti direttamente da Firebase

<div align=center>
 <img width="200" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/3f76ca07-1477-4fae-8c7d-1df28d8ec4e9">
</div>

__GenerateFragment__: Permette all'utente di creare un post con la t-shirt personalizzata, il flusso di azioni per la creazione sono:

* Descrivere l'immagine desiderata nell'apposita EditText
* Premere il pulsante di fianco l'EditText per confermare
* L'applicazione genera l'immagine e scorrendo verso sinistra è possibile vederla applicata ad una maglietta
* Ora è possibile, attraverso appositi bottoni sottostanti, acquistare la maglietta nello shop (vedere sezione che segue)
  o condividere con la community il post raffigurante l'articolo appena generato

<div align=center>
 <img width="600" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/8efdabd9-f61d-4382-b031-f0468d70be1e">
</div>

### LA FEATURE BUY

Cliccando il pulsante "Buy" nella sezione Generale, l'utente verrà reindirizzato alla pagina dello shop dove potrà scegliere la taglia e anche diversi capi come magliette, felpe o pantaloni da poter acquistare

<div align=center>
 <img width="550" alt="Senza titolo" src="https://github.com/scio97/AiPrint/assets/56976553/52364271-4d70-4737-a165-5a1bdb9fe818">
</div>




