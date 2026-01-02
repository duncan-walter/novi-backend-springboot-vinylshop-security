# Inleiding

De VinylShop API heeft inmiddels al goed vorm gekregen. Je hebt een API met verschillende functionaliteiten, verschillende entiteiten en complexe relaties.
In de vorige opdracht heb je testen geschreven om de kwaliteit van de API te garanderen. 

Als we de huidige applicatie zouden deployen, kan alle opgeslagen data aangepast worden door iedereen die het IP-adres van de applicatie kent. In deze opdracht ga je daarom een security-laag toevoegen aan de applicatie, zodat alleen geautoriseerde gebruikers bij je data kunnen komen.

Heb je de opdracht van vorige week niet gemaakt of niet af gekregen, dan kun je de [voorbeeld uitwerkingen](https://github.com/hogeschoolnovi/backend-springboot-vinylshop-testen-uitwerkingen) clonen.


# Opdrachtbeschrijving

Je gaat in deze opdracht verder werken aan de opdracht die je vorige week gemaakt hebt.

Je begint met een KeyCloak omgeving opzetten. 

Vervolgens voeg je de security toe aan je code en ga je zorgen dat gebruikers een persoonlijke lijst van albums kunnen bij houden door een `Profile` entiteit te maken.

# Randvoorwaarden

- Je voldoet aan de randvoorwaarden van de vorige opdracht.

[//]: # (TODO)

# Stappenplan

## Stap 1 (Keycloak)

Initialiseer keycloak door het te downloaden en op te starten in "dev" mode op poort `9090`. 
Wanneer je voor het eerst KeyCloak start, moet je een admin login aanmaken. Kies daar iets simpels als: 
- Username: admin
- Password: admin

Vervolgens kun je inloggen met dat account. 

### Stap 1.1 (Realm)

Maak een `VinylShop`-Realm aan.

### Stap 1.2 (Users)

Maak 2 test-gebruikers: 
- username: novi
- password: geheim

en 

- username: java
- password: password

Test of het werk door in te loggen als deze test-gebruikers op `localhost:9090/realms/VinylShop/account`.

### Stap 1.3 (Client)
Maak in het Realm een Client aan voor de VinylShop backend applicatie. 

#### General settings
Als client-name mag je `VinylShopBackend` gebruiken.

#### Capability config
Zorg dat de `Client authentication ` aan staat. 
Zorg ook dat je de `Standaard Flow` en `Service Account Role` geselecteerd hebt. 

#### Login settings
Vul hier tenminste de `valid redirects` in met:
- https://www.keycloak.org/app/*
- https://oauth.pstmn.io/v1/callback

en om alle origins toe te staan, vul je de `web origins` in met:
- /* 

## Stap 2 (Dependency)

Voeg de volgende dependencies toe aan je `pom.xml` 

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

Herlaad maven en herstart de applicatie. 

Probeer om een van je endpoints aan te spreken via PostMan. Als het goed is krijg je een 401 response terug. 

## Stap 3 (properties)

Vul de `application.properties` aan met alle benodigde informatie van de client die je zojuist bij KeyCloak hebt geregistreerd.

```properties
client-id = VinylShop
issuer-uri = http://localhost:9090/realms/${client-id}
client-secret = K9NyX15pd6Bzmd8iONOcqnwNCtSD23i6
audience = account
spring.security.oauth2.resourceserver.jwt.issuer-uri= ${issuer-uri}
spring.security.oauth2.resourceserver.jwt.audiences= ${audience}
```

Let op dat de `client-id` en `client-secret` voor jou registratie wellicht anders zijn dan in dit voorbeeld. Pas dit aan wanneer nodig. 

## Stap 4 (Security code)
Kopieer de `config` map (en de twee java-klassen daarin) naar je eigen project. 

Je hebt nu een werkende security configuratie, maar het is nog niet voldoende beveiligd. Test het maar eens door een endpoint aan te spreken met Postman. Als het goed is kun je alle endpoints weer aanspreken, zoals je dat vorige week ook kon. 

We hebben natuurlijk niet al die moeite gedaan om security in te bouwen, om vervolgens alles alsnog open te zetten. We hebben nu de beschikking over `RequestMatchers` om nauwgezette toegangscontrole uit te kunnen oefenen op individuele (of groepen) endpoints. Laten we beginnen door `.anyRequest().permitAll()` te vervangen met `.anyRequest().authenticated()`

Als je nu (opnieuw opstart en) in Postman een endpoint probeert aan te spreken, krijg je weer een 401 response.

We gaan straks preciezere controle uitoefenen op de endpoints, maar eerst gaan we naar PostMan.

## Stap 5 (Postman)
Nu je `.anyRequest().authenticated()` hebt ingesteld, zul je dus moeten authenticeren om in te kunnen loggen. Je kunt Postman zo instellen dat deze automatisch een JWT token opvraagt bij KeyCloak met de credentials van jouw gebruiker.

Ga naar het `Authorization` tabblad van je collectie en kies `OAuth2.0`.
Scroll naar beneden tot je bij het kopje **Configure New Token** bent. Daar vul je in: 
- **Token name**: vinylshoptoken
- **Grant type**: Authorization Code
- **Callback URL**: 
  - Als je de browser gebruikt vul je in: "https://oauth.pstmn.io/v1/browser-callback"
  - Als je de desktop applicatie gebruikt vink je aan: "Authorize using browser"
- **Auth URL**: Vul hier de `authorization_endpoint` in die je kunt vinden via het [.well-known endpoint](http://localhost:9090/realms/VinylShop/).well-known/openid-configuration. 
- **Access Token URL**: Vul hier de `token_endpoint` in die je kunt vinden via het [.well-known endpoint](http://localhost:9090/realms/VinylShop/).
- **Client ID**: Deze kun je vinden op je admin dashboard van KeyCloak `Clients -> [jouw client]`. Daar staat het Client Id op de voorpagina.
- **Client Secret**: Deze kun je vinden op je admin dashboard van KeyCloak `Clients -> [jouw client] -> Credentials`. Daar kun je het Client Secret kopieren en in postman plakken.
- **Scope**: Vul hier in ieder geval `openid` in. Andere mogelijke scopes kun je in het [.well-known endpoint](http://localhost:9090/realms/VinylShop/) vinden.

Klik vervolgens op "Get New Accces Token". Log in de browser in met een van je test-gebruikers, wanneer je weer teruggeleid wordt naar Postman, accepteer je het token door op "use token" te klikken.

Spreek nu weer één van je endpoints aan. Zorg dat je daar in het "authorization" tabblad een van de volgende opties selecteert:
- "Inherit auth from parent", als je netjes in een collectie werkt.
- "oauth2.0", waarbij je geen nieuw token maakt, maar het zojuist gemaakte token selecteert.

Je zult merken dat je weer gewoon je endpoints kunt aanspreken. 

Er is nu nog geen verschil tussen je gebruikers. Elke ingelogde gebruiker, kan elk endpoint aanspreken. Laten we daar verandering in brengen.

## Stap 6 (Rollen)

Om op autorisatie-niveau onderscheid te kunnen maken tussen gebruikers, zullen we gebruik maken van rollen. Met een mooi woord heet dit RBAC (Role Based Acces Control). 

Maak een rol "ROLE_USER" en een rol "ROLE_ADMIN" aan in je realm of client. Let op, die "ROLE_" prefix is belangrijk.

Wijs de ADMIN rol toe aan de "novi"-gebruiker en wijs de USER rol toe aan de "java"-gebruiker. 

> Tip: Gebruik de zoek functie van het "role-mapping" tabblad om te zoeken op je client-naam.

Je kunt nu opnieuw een token aanvragen in Postman (je hoeft niet alles opnieuw in te vullen, je kunt gewoon op "Get New Acces Token" klikken). Met dit nieuwe wanneer je dit nieuwe token in https://www.jwt.io/ invult, kun je zien dat jouw rol in de token is ingevuld.

## Stap 7 (RequestMatchers)

Nu is het tijd om specifieker te zijn in de toegangsrechten die we willen verlenen. Pas de volgende regels toe: 
- Stock mag allen door ADMIN worden opgehaald, aangepast, verwijderd of creëren.
- Albums mogen door iedereen worden opgehaald (publiek)
- Voor alle entiteiten mag alleen de ADMIN creëren, verwijderen of aanpassen.
- Alle entiteiten (behalve stock) mogen door alle ingelogde gebruikers opgehaald worden.

Test met Postman of je instellingen toelaten wie toegelaten mag worden en blokkeren wie geblokkeerd moet worden.

> Tip: Lukt het niet om een token voor een andere gebruiker op te slaan in Postman? Voeg dan de parameter `prompt=login` toe achter je `authorization URL`. Vergeet niet om die token ook een andere naam te geven.

## Stap 8 (BONUS: Profile)

We hebben nu een werkende applicatie met werkende authorisatie en authenticatie via Keycloak. Dat is fantastisch, maar we hebben het nog niet echt praktisch toegepast in onze applicatie. Voor deze (BONUS) opdracht ga je een `Profile` entiteit maken, waarin een gebruiker diens favoriete albums kan opslaan. Wanneer een gebruiker dan inlogt, weet de applicatie wie de gebruiker is en kan de applicatie de favoriete albums van die gebruiker laten zien. Omdat dit op je authenticatie is gebaseerd, kan een andere gebruiker niet jou favoriete albums inzien of wijzigen. Een admin kan dat natuurlijk wel jou `Profile` inzien, aanpassen en eventueel verwijderen.

## Stap 8.1 (Profile Entity)
Maak een profile Entity

```java
public class ProfileEntity extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String kcid;

    @ManyToMany
    List<AlbumEntity> albums = new ArrayList<>();
    
    // Getters en Setters
}
```

## Stap 8.2 (Profile Repository)
Maak een Profile Repository

```java
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    boolean existsByKcid(String kcid);
    Optional<ProfileEntity> findByKcid(String kcid);
}
```

## Stap 8.3 (Profile Mapper en Dto's)
Maak Dto's en een Mapper:

```java
public class ProfileAlbumRequestDto {

    private Long albumId;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
}
```

```java
public class ProfileResponseDto {
    private Long id;
    private String kcid;
    private List<Long> albums;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKcid() {
        return kcid;
    }

    public void setKcid(String kcid) {
        this.kcid = kcid;
    }

    public List<Long> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Long> albums) {
        this.albums = albums;
    }
}
```

```java
@Component
public class ProfileDTOMapper {

    public ProfileResponseDto mapToDto(ProfileEntity entity){
        ProfileResponseDto dto = new ProfileResponseDto();
        if(entity.getAlbums()!=null) {
            dto.setAlbums(entity.getAlbums()
                    .stream()
                    .map(BaseEntity::getId)
                    .toList());
        }
        dto.setId(entity.getId());
        dto.setKcid(entity.getKcid());

        return dto;
    }

    public List<ProfileResponseDto> toDto(List<ProfileEntity> entity){
        return entity.stream().map(this::toDto).toList();
    }
}
```

## Stap 8.4 (Profile Service)
Maak een Profile Service

```java
package nl.novi.vinylshop.services;

import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.dtos.profile.ProfileResponseDto;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.ProfileEntity;
import nl.novi.vinylshop.mappers.AlbumDTOMapper;
import nl.novi.vinylshop.mappers.ProfileDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;

import jakarta.persistence.EntityNotFoundException;
import nl.novi.vinylshop.repositories.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileDTOMapper profileMapper;
    private final AlbumRepository albumRepository;
    private final AlbumDTOMapper albumMapper;

    public ProfileService(ProfileRepository profileRepository, ProfileDTOMapper profileMapper, AlbumRepository albumRepository, AlbumDTOMapper albumMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }

    public List<ProfileResponseDto> findAllProfiles() {
        List<ProfileEntity> profileEntities = profileRepository.findAll();
        return profileEntities.stream()
                .map(profileMapper::mapToDto)
                .toList();
    }

    public ProfileResponseDto findOrCreateProfile(Authentication authentication) {
        var kcid = authentication.getName();
        return profileMapper.mapToDto(findOrCreateProfileEntity(kcid));
    }

    private ProfileEntity findOrCreateProfileEntity(String kcid){
        if(profileRepository.existsByKcid(kcid)) {
            return profileRepository.findByKcid(kcid).get();
        } else {
            return createProfileEntity(kcid);
        }
    }

    private ProfileEntity createProfileEntity(String kcid) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setKcid(kcid);
        return profileRepository.save(profileEntity);
    }

    public void linkAlbum(Authentication authentication, Long albumId) {
        var kcid = authentication.getName();
        AlbumEntity album = albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album met ID " + albumId + " not found"));
        ProfileEntity profileEntity = findOrCreateProfileEntity(kcid);

        profileEntity.getAlbums().add(album);
        profileRepository.save(profileEntity);
    }

    public List<AlbumResponseDTO> getAlbumForProfile(String kcid) {
        var profile = findOrCreateProfileEntity(kcid);
        List<AlbumEntity> albums = profile.getAlbums();
        if (albums == null || albums.isEmpty()) {
            return List.of();
        }
        return albumMapper.mapToDto(albums);
    }
}

```

## Stap 8.5 (Profile Controller)
Maak een Profile Controller

```java
@RestController()
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {this.profileService = profileService;}
    
    @GetMapping()
    public ResponseEntity<List<ProfileResponseDto>> getProfiles(Authentication authentication) {

        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());;

        if(authorities.contains("ROLE_ADMIN")){
            List<ProfileResponseDto> profileDtos = profileService.findAllProfiles();
            return ResponseEntity.ok(profileDtos);
        } else if (authorities.contains("ROLE_USER")){
            ProfileResponseDto profileDto = profileService.findOrCreateProfile(authentication);
            return ResponseEntity.ok(List.of(profileDto));
        } else {
            return ResponseEntity.status(403).build();
        }

    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumResponseDTO>> getAlbum(Authentication authentication){
        String kcid = authentication.getName();
        return ResponseEntity.ok(profileService.getAlbumForProfile(kcid));
    }

    @PostMapping("/albums")
    public ResponseEntity<Void> linkAlbum(Authentication authentication, @RequestBody ProfileAlbumRequestDto dto){
        profileService.linkAlbum(authentication, dto.getAlbumId());
        return ResponseEntity.ok().build();
    }
}
```

## Stap 8.6 (Profile gebruiken)
Gebruik Postman om te testen of je Profile functionaliteiten werken. 

- Log in als `java` en voeg een favoriet album toe.
- Log in als een nieuwe `test` gebruiker en voeg een album toe. 
- Vraag als `test` je albums en je profiel op en zie dat je niks van `java` ziet. 
- Vraag als `novi` je profielen op en zie alles vn zowel `java` als `test`.

## Stap 9 (BONUS testen)

Dankzij je security, slagen niet al je testen meer. 

Zorg dat al je testen weer slagen. Kijk in het "testen" hoofdstuk op EdHub welke annotatie(-aanpassing) je daar voor nodig hebt.


