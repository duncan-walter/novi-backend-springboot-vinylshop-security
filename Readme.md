# Inleiding

Je hebt in de vorige opdracht de architectuur van de VinylShop applicatie compleet gemaakt door een controller-service-repository patroon te implementeren.
Ook heb je de data gestructureerd, zodat elke laag met een eigen datamodel communiceert.  
In deze opdracht ga je nog drie entiteiten aan de casus toevoegen, je gaat voor al deze entiteiten nog eens oefenen om de complete architectuur uit te werken en datamodellen te maken. 
Daarnaast ga je ook alle entiteiten met elkaar koppelen door middel van relaties. Ook de entiteiten die je al had, zul je daarvoor moeten aanpassen. 

Heb je de opdracht van vorige week niet gemaakt of niet af gekregen, dan kun je de [voorbeeld uitwerkingen](https://github.com/hogeschoolnovi/backend-springboot-vinylshop-modellen-uitwerkingen) clonen.


# Opdrachtbeschrijving

Je gaat in deze opdracht verder werken aan de opdracht die je vorige week gemaakt hebt.
Je gaat de collectie entiteiten fors uitbreiden door de volgende entiteiten te maken die allen overerven van de `BaseEntity`: 
- AlbumEntity
- ArtistEntity
- StockEntity

Je gaat relaties leggen tussen deze entiteiten, zodat het er als volgt uit ziet:

[//]: # (TODO: Klassendiagram relaties)

# Randvoorwaarden

- Zorg dat je mappenstructuur aan de Maven voorwaarden voldoet
- Je mappen structuur heeft ten minste de packages: 
  - controllers
  - repositories
  - services
  - dtos
  - models 
  - entities
- Je hebt vijf controllers:
    - GenreController
    - PublisherController
    - AlbumController
    - ArtistController
    - StockController
- Je hebt vijf service:
    - GenreService
    - PublisherService
    - AlbumService
    - ArtistService
    - StockService
- Je hebt vijf repositories:
    - GenreRepository
    - PublisherRepository
    - AlbumRepository
    - ArtistRepository
    - StockRepository
- Je hebt zes entiteiten:
    - BaseEntity
    - GenreEntity
    - PublisherEntity
    - AlbumEntity
    - ArtistEntity
    - StockEntity
- Je hebt voor alle vijf entiteiten ten minste twee DTO's:
  - ...RequestDTO
  - ...ResponseDTO
- Je hebt zes models
  - GenreModel
  - PublisherModel
  - AlbumModel
  - ArtistModel
  - StockModel
  - BaseModel
- Je hebt vier mappers
  - GenreDTOMapper
  - GenreEntityMapper
  - PublisherDTOMapper
  - PublisherEntityMapper
  
- Je hebt de juiste instellingen in je application.properties om de database connectie goed tot stand te brengen.
- Je hebt de juiste dependencies aan het project gekoppeld.
- Je hebt minimaal één record van alle entiteiten in je data.sql staan.
- Je hebt een export van Postman met up-to-date requests voor alle entiteiten, inclusief het koppelen van relaties.

# Stappenplan

## Stap 1

Maak eerst de kale `AlbumEntity`, `ArtistEntity` en `StockEntity` zonder relaties, die voegen we later toe.

De AlbumEntity ziet er (voor nu) zo uit: 
```java
@Entity
@Table(name = "albums")
public class AlbumEntity extends BaseEntity {
    private String title;
    private int releaseYear;
//    Getters, setters en constructors
}
```

De ArtistEntity zo: 
```java
@Entity
@Table(name = "artists")
public class ArtistEntity extends BaseEntity {
    private String name;
    private String biography;
//    Getters, setters en constructor
}
```

en de StockEntity zo:
```java
@Entity
@Table(name = "stock")
public class StockEntity extends BaseEntity {
    private String condition;
    private double price;
//    Getters, setters en constructors
}
```

## Stap 2 (entiteiten en hun architectuur)

Voeg voor al deze entiteiten ook een Controller, Service en Repository toe. 

> Tip: het is makkelijk om onderaan de architectuur te beginnen, omdat een repository alleen een entity nodig heeft, maar ee nservice heeft een repository nodig en een controller heeft een service nodig.

De repositories mag je extra functionaliteiten geven, boven op de standaard functionaliteiten van JPA:

**StockRepository**
Deze repository krijgt 3 extra functies:
- Optional<StockEntity> findByIdAndAlbumId(Long id, Long albumId);
- void deleteByIdAndAlbumId(Long id, Long albumId);
- List<StockEntity> findByAlbumId(Long albumId);

**ArtistRepository**
Deze repository krijgt 1 extra functie, met de @Query annotatie:
```java
	@Query("SELECT DISTINCT a FROM ArtistEntity a JOIN a.albums al WHERE al.id = :albumId")
    List<ArtistEntity> findArtistsByAlbumId(@Param("albumId") Long albumId);
```

**AlbumRepository**
Deze repository krijgt ook één extra functionaliteit met de @Query annotatie:
```java
    @Query("SELECT DISTINCT a FROM AlbumEntity a JOIN FETCH a.stockItems WHERE a.stockItems IS NOT EMPTY")
    List<AlbumEntity> findAlbumsWithStock();
```

Zorg dat de controllers ten minste de volgende endpoints hebben: 
- GET (all)
- GET (one)
- POST
- PUT
- DELETE

Weet je niet meer hoe je de service, controller en repository maakt, kijk dan nog eens bij de vorige opdracht.

Het is natuurlijk lastig om de controller en service te maken, zonder DTO, model en zelfs de mappers heb je er bij nodig.
Probeer met één functionaliteit te beginnen, bijvoorbeeld de "get-all". Van daaruit kun je verder bouwen met de volgende functionaliteit. 

Het lijkt veel werk, dat is het ook, maar als je het systematisch aanpakt, kun je het stap voor stap oplossen.

## Stap 3 (relaties in de entiteiten)

Voeg relaties toe in de entiteiten. Doe dat op de volgende manier:
- Een Album heeft 1 publisher, een Publisher heeft meerdere Albums
- Een Album heeft meerdere Stocks, een Stock heeft 1 Album
- Een Album heeft meerdere Artists, een Artist heeft meerdere Albums
- Een Album heeft 1 Genre, een Genre heeft geen Albums (dit is een unidirectionele relatie)

Gebruik de `@OneToMany`, `@ManyToOne` en `@ManyToMany` annotaties. 
Vergeet ook niet de `mappedBy` op de juiste plekken in te vullen.

## Stap 4 (relaties in de models en DTO's) 

## Stap 5 (Functionaliteit)

