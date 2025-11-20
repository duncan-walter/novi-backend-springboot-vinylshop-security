# Inleiding
Je vriend, een fervent verzamelaar en handelaar in vinyl, staat op het punt een droom te realiseren: het lanceren van een website om zijn collectie te delen met de wereld. Hoewel de frontend van de website in goede handen is, ontbreekt het nog aan een belangrijk onderdeel - de backend. Hier kom jij in het spel. 

Je hebt inmiddels als het een en ander geleerd over SpringBoot en het opzetten van een SpringBoot project. Je hebt ook geleerd om een simpele controller te bouwen. Die opgedane kennis ga je in deze opdracht toepassen.

# Opdrachtbeschrijving

De VinylShop-applicatie zal een complexe applicatie worden met meerdere architecturale lagen, validatie, een complexe database structuur, upload/download, security en testen. De opdracht zal er uiteindelijk ongeveer zo uit komen te zien: 
<details>
<summary> Mappenstructuur</summary>

```terminaloutput

src
├── main
│   ├── java
│   │   └── com
│   │       └── vinylWebshop
│   │           └── vinylcollectie
│   │               ├── VinylcollectieApplication.java
│   │               ├── config
│   │               │   ├── JwtAudienceValidator.java
│   │               │   └── SecurityConfig.java
│   │               ├── controllers
│   │               │   ├── AlbumController.java
│   │               │   ├── AlbumCoverController.java
│   │               │   ├── ArtistController.java
│   │               │   ├── GenreController.java
│   │               │   ├── InventoryController.java
│   │               │   ├── ProfileController.java
│   │               │   ├── PublisherController.java
│   │               │   └── StockController.java
│   │               ├── dto
│   │               │   ├── Inventory
│   │               │   │   └── InventoryRequestDTO.java
│   │               │   ├── album
│   │               │   │   ├── AlbumArtistRequestDTO.java
│   │               │   │   ├── AlbumExtendedResponseDTO.java
│   │               │   │   ├── AlbumRequestDTO.java
│   │               │   │   └── AlbumResponseDTO.java
│   │               │   ├── albumCover
│   │               │   │   ├── AlbumCoverDTOMapper.java
│   │               │   │   ├── AlbumCoverRequestDTO.java
│   │               │   │   └── AlbumCoverResponseDTO.java
│   │               │   ├── artist
│   │               │   │   ├── ArtistRequestDTO.java
│   │               │   │   └── ArtistResponseDTO.java
│   │               │   ├── genre
│   │               │   │   ├── GenreRequestDTO.java
│   │               │   │   └── GenreResponseDTO.java
│   │               │   ├── profile
│   │               │   │   ├── ProfileAlbumRequestDto.java
│   │               │   │   └── ProfileResponseDto.java
│   │               │   ├── publisher
│   │               │   │   ├── PublisherRequestDTO.java
│   │               │   │   └── PublisherResponseDTO.java
│   │               │   ├── stock
│   │               │   │   ├── StockRequestDTO.java
│   │               │   │   └── StockResponseDTO.java
│   │               │   ├── token
│   │               │   └── users
│   │               ├── entities
│   │               │   ├── AlbumCoverEntity.java
│   │               │   ├── AlbumEntity.java
│   │               │   ├── ArtistEntity.java
│   │               │   ├── BaseEntity.java
│   │               │   ├── GenreEntity.java
│   │               │   ├── ProfileEntity.java
│   │               │   ├── PublisherEntity.java
│   │               │   └── StockEntity.java
│   │               ├── helpers
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   └── UrlHelper.java
│   │               ├── mappers
│   │               │   ├── dto
│   │               │   │   ├── AlbumDTOMapper.java
│   │               │   │   ├── AlbumExtendedDTOMapper.java
│   │               │   │   ├── ArtistDTOMapper.java
│   │               │   │   ├── GenreDTOMapper.java
│   │               │   │   ├── InventoryMapper.java
│   │               │   │   ├── ProfileDTOMapper.java
│   │               │   │   ├── PublisherDTOMapper.java
│   │               │   │   └── StockDTOMapper.java
│   │               │   └── entity
│   │               │       ├── AlbumCoverEntityMapper.java
│   │               │       ├── AlbumEntityMapper.java
│   │               │       ├── ArtistEntityMapper.java
│   │               │       ├── EntityMapper.java
│   │               │       ├── GenreEntityMapper.java
│   │               │       ├── ProfileEntityMapper.java
│   │               │       ├── PublisheEntityMapper.java
│   │               │       └── StockEntityMapper.java
│   │               ├── models
│   │               │   ├── AlbumCoverModel.java
│   │               │   ├── AlbumModel.java
│   │               │   ├── ArtistModel.java
│   │               │   ├── BaseModel.java
│   │               │   ├── GenreModel.java
│   │               │   ├── ProfileModel.java
│   │               │   ├── PublisherModel.java
│   │               │   └── StockModel.java
│   │               ├── repositories
│   │               │   ├── AlbumCoverRepository.java
│   │               │   ├── AlbumRepository.java
│   │               │   ├── ArtistRepository.java
│   │               │   ├── GenreRepository.java
│   │               │   ├── ProfileRepository.java
│   │               │   ├── PublisherRepository.java
│   │               │   └── StockRepository.java
│   │               ├── security
│   │               └── services
│   │                   ├── AlbumCoverService.java
│   │                   ├── AlbumService.java
│   │                   ├── ArtistService.java
│   │                   ├── GenreService.java
│   │                   ├── ProfileService.java
│   │                   ├── PublisherService.java
│   │                   └── StockService.java
│   └── resources
│       ├── application.properties
│       └── data.sql
└── test
    ├── java
    │   └── com
    │       └── vinylWebshop
    │           └── vinylcollectie
    │               ├── VinylcollectieApplicationTests.java
    │               ├── integrationTests
    │               │   └── GenreControllerIntegrationTest.java
    │               └── serviceUnittests
    │                   ├── AlbumCoverServiceTests.java
    │                   ├── AlbumServiceTests.java
    │                   ├── ArtistServiceTests.java
    │                   ├── GenreServiceTests.java
    │                   ├── PublisherServiceTests.java
    │                   └── StockServiceTests.java
    └── resources
        ├── application-test.properties
        └── data-test.sql
```
</details>

Geen paniek, dit hoef je niet vandaag al zo te maken. 
Laten we rustig beginnen met je eerste controller. 

Als eerste controller ga je de `GenreController` maken, omdat deze het eenvoudigst is.

Je hoeft ook nog geen gebruik te maken van een echte database, je kunt in deze GenreController gebruik maken van de tijdelijke `GenreService` om een mock-database te gebruiken die `Genre` objecten kan opslaan en bewerken.

De `Genre` en `GenreService` klassen kun je uit de respository halen en in je eigen project zetten.

# Randvoorwaarden

De opdracht bevat tenminste: 
- Een SpringBoot project structuur op basis van Maven
- Een `controllers` map, een `services` map en een `entities` map. Optioneel ook een `helpers` map.
- Een GenreController met daarin:
  - GET /genres - Haalt een lijst van alle genres op. 
  - GET /genres/{id} - Haalt een specifiek genre op basis van ID op. 
  - POST /genres - Creëert een nieuw genre. 
  - PUT /genres/{id} - Werkt een bestaand genre bij. 
  - DELETE /genres/{id} - Verwijdert een genre.
- Een export van je Postman collectie

# Stappenplan

## Stap 1
- Zet een nieuw SpringBoot project op via https://start.spring.io/ of de ingebouwde Spring Initializr van IntelliJ. 
- Zorg dat dit project gemaakt wordt op basis van Maven en een LTS versie van Java. 
- Vul de metadata van de pom.xml met logische waarden (group, artifact, name, etc)

## Stap 2
Neem in je pom.xml de juiste dependency op om een RestController te kunnen maken (spring-boot-starter-web).

## Stap 3
Maak in je `src.main.java.[group].[artifact]` drie nieuwe mappen aan.
- controllers
- services
- entities

Kopieer de `GenreService` klasse uit deze repository naar de `services` map in jou project.  
Kopieer de `Genre` klasse uit de repository naar de `entities` map in jou project.

## Stap 4
- Maak in je `controllers` map een nieuwe klasse `GenreController`.  
- Geef deze klasse de juiste annotaties
  - één annotatie om aan te geven dat deze controller volgens het REST protocol werkt.
  - één annotatie om aan te geven dat de endpoints in deze controller met "/genres" begint
- Maak gebruik van "constructor injection" om de `GenreService` in de `GenreController` te injecteren.

## Stap 5
Maak alle CRUD requests aan voor de Genre entiteit.  
Gebruik hierbij de methodes uit de `GenreService`.

| Http Methode | GenreService Methode |
|--------------|----------------------|
| GET (one)    |            findGenreById          |
| GET (all)    |         findAllGenres             |
| POST         | createGenre                     |
| PUT          |      updateGenre                |
| DELETE       |          deleteGenre            |

## Stap 6 (Bonus)
Zorg voor een juiste status code als response.  
Je kunt dit voor elkaar krijgen door gebruik te maken van de `ResponseEntity` of van de `@ResponseStatus`.

Bij de POST mapping betekent dit dat je ook de "Location header" moet meesturen.
Je kunt dit met behulp van de `UrlHelper` klasse doen in deze repository, of je gebruikt `URI.create("het pad waar je het nieuwe object kunt ophalen")`. 

Gebruik je de `UrlHelper`, dan zet je die uiteraard netjes in een `helpers` map.

