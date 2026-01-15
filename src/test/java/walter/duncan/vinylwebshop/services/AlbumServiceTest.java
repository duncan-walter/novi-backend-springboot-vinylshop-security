package walter.duncan.vinylwebshop.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.*;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolation;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolationException;
import walter.duncan.vinylwebshop.exceptions.ResourceNotFoundException;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.mappers.AlbumExtendedDtoMapper;
import walter.duncan.vinylwebshop.repositories.AlbumRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {
    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumDtoMapper albumDtoMapper;

    @Mock
    private AlbumExtendedDtoMapper albumExtendedDtoMapper;

    @Mock
    private GenreService genreService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private ArtistService artistService;

    @Test
    void findAllAlbums_shouldReturnListOfAlbumExtendedResponseDtos() {
        // Arrange
        var albumExtendedResponseDtos = List.of(
                AlbumTestData.albumExtendedResponseDto(1L, 1L),
                AlbumTestData.albumExtendedResponseDto(1L, 2L)
        );

        var genreEntity = GenreTestData.genreEntity(1L);
        var publisherEntity = PublisherTestData.publisherEntity(1L);

        var stockEntity = new StockEntity();
        ReflectionTestUtils.setField(stockEntity, "id", 1L);
        stockEntity.setCondition("Used");
        stockEntity.setPrice(10.0);

        var artistEntity = new ArtistEntity();
        ReflectionTestUtils.setField(artistEntity, "id", 1L);
        artistEntity.setName("Mr. Skeleton");
        artistEntity.setBiography("He has lived his year spooking many people with his music, now he's dead. Serves him right.");

        var albumEntity1 = AlbumTestData.albumEntity(
                1L,
                "The Nightmare Before Christmas",
                1993
        );
        albumEntity1.setGenre(genreEntity);
        albumEntity1.setPublisher(publisherEntity);
        albumEntity1.setStockItems(new HashSet<>(List.of(stockEntity)));
        albumEntity1.setArtists(new HashSet<>(List.of(artistEntity)));

        var albumEntity2 = AlbumTestData.albumEntity(
                2L,
                "The Nightmare Before Christmas 2",
                1993
        );
        albumEntity2.setGenre(genreEntity);
        albumEntity2.setPublisher(publisherEntity);
        albumEntity2.setStockItems(new HashSet<>(List.of()));
        albumEntity2.setArtists(new HashSet<>(List.of(artistEntity)));

        var albumEntities = List.of(
                albumEntity1,
                albumEntity2
        );

        when(albumRepository.findAll()).thenReturn(albumEntities);
        when(albumExtendedDtoMapper.toDto(albumEntities)).thenReturn(albumExtendedResponseDtos);

        // Act
        var result = albumService.findAllAlbums();

        // Assert
        assertEquals(2, result.size());
        assertEquals(albumExtendedResponseDtos.getFirst(), result.getFirst());
        assertEquals(albumExtendedResponseDtos.get(1), result.get(1));
        verify(albumRepository, times(1)).findAll();
        verify(albumExtendedDtoMapper, times(1)).toDto(albumEntities);
    }

    @Test
    void findAlbumById_shouldReturnExtendedAlbumDto() {
        // Arrange
        var albumId = 2L;
        var albumExtendedResponseDto = AlbumTestData.albumExtendedResponseDto(albumId, 2L);
        var albumEntity = AlbumTestData.albumEntity(
                albumId,
                "The Nightmare Before Christmas",
                1993
        );

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));
        when(albumExtendedDtoMapper.toDto(albumEntity)).thenReturn(albumExtendedResponseDto);

        // Act
        var result = albumService.findAlbumById(albumId);

        // Assert
        assertEquals(albumExtendedResponseDto, result);
        verify(albumRepository, times(1)).findById(albumId);
        verify(albumExtendedDtoMapper, times(1)).toDto(albumEntity);
    }

    @Test
    void findAlbumById_withNonExistingAlbumId_shouldThrowResourceNotFoundException() {
        // Arrange
        var albumId = 1337L;

        when(albumRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.findAlbumById(albumId)
        );
    }

    @Test
    void createAlbum_withExistingGenreAndPublisher_shouldReturnCreatedAlbumResponseDto() {
        // Arrange
        var albumId = 1L;
        var albumTitle = "The Nightmare Before Christmas";
        var albumReleaseYear = 1993;
        var genreId = 1L;
        var publisherId = 1L;

        var albumRequestDto = AlbumTestData.albumRequestDto(albumTitle, albumReleaseYear);
        var albumResponseDto = AlbumTestData.albumResponseDto(albumId, albumTitle, albumReleaseYear);
        var mappedAlbumEntity = AlbumTestData.albumEntity(albumTitle, albumReleaseYear);
        var createdAlbumEntity = AlbumTestData.albumEntity(albumId, albumTitle, albumReleaseYear);
        var genreEntity = GenreTestData.genreEntity(genreId);
        var publisherEntity = PublisherTestData.publisherEntity(publisherId);

        when(albumRepository.save(mappedAlbumEntity)).thenReturn(createdAlbumEntity);
        when(albumDtoMapper.toEntity(albumRequestDto)).thenReturn(mappedAlbumEntity);
        when(genreService.getExistingById(genreId)).thenReturn(genreEntity);
        when(publisherService.getExistingById(publisherId)).thenReturn(publisherEntity);
        when(albumDtoMapper.toDto(createdAlbumEntity)).thenReturn(albumResponseDto);

        // Act
        var result = albumService.createAlbum(albumRequestDto);

        // Assert
        assertEquals(albumResponseDto, result);
        verify(albumRepository, times(1)).save(mappedAlbumEntity);
        verify(genreService, times(1)).getExistingById(genreId);
        verify(publisherService, times(1)).getExistingById(publisherId);
    }

    @Test
    void updateAlbum_withNewTitleAndReleaseYear_shouldReturnUpdatedAlbumResponseDto() {
        // Arrange
        var albumId = 1L;
        var newAlbumTitle = "Updated title";
        var newAlbumReleaseYear = 2000;

        var albumRequestDto = AlbumTestData.albumRequestDto(newAlbumTitle, newAlbumReleaseYear);
        var albumEntity = AlbumTestData.albumEntity(albumId, "The Nightmare Before Christmas", 1993);
        var albumResponseDto = AlbumTestData.albumResponseDto(albumId, newAlbumTitle, newAlbumReleaseYear);

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));
        when(albumRepository.save(any(AlbumEntity.class))).thenReturn(albumEntity);
        when(albumDtoMapper.toDto(albumEntity)).thenReturn(albumResponseDto);

        // Act
        var result = albumService.updateAlbum(albumId, albumRequestDto);

        // Assert
        assertEquals(albumResponseDto, result);
        assertEquals(newAlbumTitle, albumEntity.getTitle());
        assertEquals(newAlbumReleaseYear, albumEntity.getReleaseYear());
    }

    @Test
    void updateAlbum_withSameGenreAndPublisher_shouldNotCallGetExistingById() {
        // Arrange
        var albumId = 1L;
        var newAlbumTitle = "Updated title";

        var albumEntity = AlbumTestData.albumEntity(albumId, "The Nightmare Before Christmas", 1993);
        var albumRequestDto = AlbumTestData.albumRequestDto(newAlbumTitle, albumEntity.getReleaseYear());
        var genreEntity = albumEntity.getGenre();
        var publisherEntity = albumEntity.getPublisher();

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));
        when(genreService.isSameById(genreEntity, genreEntity.getId())).thenReturn(true);
        when(publisherService.isSameById(publisherEntity, publisherEntity.getId())).thenReturn(true);

        // Act
        albumService.updateAlbum(albumId, albumRequestDto);

        // Assert
        verify(genreService, never()).getExistingById(anyLong());
        verify(publisherService, never()).getExistingById(anyLong());
    }

    @Test
    void deleteAlbum_withExistingAlbum_shouldDeleteAlbum() {
        // Arrange
        var albumId = 1L;
        var persistedAlbumEntity = AlbumTestData.albumEntity(albumId, "Makes you think", 2026);

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(persistedAlbumEntity));

        // Act
        albumService.deleteAlbum(albumId);

        // Assert
        verify(albumRepository, times(1)).findById(albumId);
        verify(albumRepository, times(1)).deleteById(albumId);
    }

    @Test
    void deleteAlbum_withExistingStockOnAlbum_shouldThrowBusinessRuleViolationException() {
        // Arrange
        var albumId = 1L;
        var albumEntity = AlbumTestData.albumEntity(albumId, "The Nightmare Before Christmas", 1993);
        var stockEntity = new StockEntity();

        stockEntity.setAlbum(albumEntity);
        stockEntity.setCondition("Brand spanking new");
        stockEntity.setPrice(100.0);
        albumEntity.setStockItems(new HashSet<>(List.of(stockEntity)));

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));

        // Act
        var exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> albumService.deleteAlbum(albumId)
        );

        // Assert
        assertEquals(BusinessRuleViolation.CANNOT_DELETE_ALBUM_WHEN_IT_HAS_STOCK, exception.getViolation());
    }

    @Test
    void linkArtist_withExistingArtist_shouldAddArtistToAlbum() {
        // Arrange
        var albumId = 1L;
        var artistId = 1L;
        var persistedAlbumEntity = AlbumTestData.albumEntity(albumId, "Henk and the Hillbillies", 2026);
        var persistedArtistEntity = ArtistTestData.artistEntity(artistId, "Henk", "The legendary");

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(persistedAlbumEntity));
        when(artistService.getExistingById(artistId)).thenReturn(persistedArtistEntity);

        // Act
        albumService.linkArtist(albumId, artistId);

        // Assert
        assertTrue(persistedAlbumEntity.getArtists().contains(persistedArtistEntity));
        verify(albumRepository, times(1)).save(persistedAlbumEntity);
    }

    @Test
    void unlinkArtist_withExistingArtist_shouldUnlinkArtistFromAlbum() {
        // Arrange
        var albumId = 1L;
        var artistId = 1L;
        var persistedAlbumEntity = AlbumTestData.albumEntity(albumId, "Henk and the Hillbillies", 2026);
        var persistedArtistEntity = ArtistTestData.artistEntity(artistId, "Henk", "The legendary");
        persistedAlbumEntity.setArtists(new HashSet<>(List.of(persistedArtistEntity)));

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(persistedAlbumEntity));
        when(artistService.getExistingById(artistId)).thenReturn(persistedArtistEntity);

        // Act
        albumService.unlinkArtist(albumId, artistId);

        // Assert
        assertFalse(persistedAlbumEntity.getArtists().contains(persistedArtistEntity));
        assertTrue(persistedAlbumEntity.getArtists().isEmpty());
        verify(albumRepository, times(1)).save(persistedAlbumEntity);
    }
}

// TODO: Introduce builder
//  Used in tests as a proof of concept, this can be expanded to a proper builder pattern that builds complex album relations.
class AlbumTestData {
    private AlbumTestData() { }

    public static AlbumEntity albumEntity(String title, int releaseYear) {
        return albumEntity(null, title, releaseYear);
    }

    public static AlbumEntity albumEntity(Long id, String title, int releaseYear) {
        var albumEntity = new AlbumEntity();

        if (id != null) {
            // Using reflection since there is no setter for the id field and there is no point introducing it in production code since it's not used.
            ReflectionTestUtils.setField(albumEntity, "id", id);
        }

        albumEntity.setTitle(title);
        albumEntity.setReleaseYear(releaseYear);
        albumEntity.setGenre(GenreTestData.genreEntity(1L));
        albumEntity.setPublisher(PublisherTestData.publisherEntity(1L));

        return albumEntity;
    }

    public static AlbumRequestDto albumRequestDto(String title, int releaseYear) {
        var albumRequestDto = new AlbumRequestDto();
        ReflectionTestUtils.setField(albumRequestDto, "title", title);
        ReflectionTestUtils.setField(albumRequestDto, "releaseYear", releaseYear);
        ReflectionTestUtils.setField(albumRequestDto, "genreId", 1L);
        ReflectionTestUtils.setField(albumRequestDto, "publisherId", 1L);

        return albumRequestDto;
    }

    public static AlbumResponseDto albumResponseDto(Long id, String title, int releaseYear) {
        return new AlbumResponseDto(
                id,
                title,
                releaseYear,
                new GenreResponseDto(1L, "Halloween", "Very spooky"),
                new PublisherResponseDto(1L, "Spooky records", "1 Spooky avenue", "+1 217-703-2085")
        );
    }

    public static AlbumExtendedResponseDto albumExtendedResponseDto(Long id, Long stockId) {
        return new AlbumExtendedResponseDto(
                id,
                "The Nightmare Before Christmas",
                1993,
                new GenreResponseDto(1L, "Halloween", "Very spooky"),
                new PublisherResponseDto(1L, "Spooky records", "1 Spooky avenue", "+1 217-703-2085"),
                List.of(new StockResponseDto(stockId, "Used", 10.0)),
                List.of(new ArtistResponseDto(1L, "Mr. Skeleton", "He has lived his year spooking many people with his music, now he's dead. Serves him right."))
        );
    }
}

class GenreTestData {
    private GenreTestData() { }

    public static GenreEntity genreEntity(Long id) {
        var genreEntity = new GenreEntity();

        if (id != null) {
            ReflectionTestUtils.setField(genreEntity, "id", id);
        }

        genreEntity.setName("Halloween");
        genreEntity.setDescription("Very spooky");

        return genreEntity;
    }
}

class PublisherTestData {
    private PublisherTestData() { }

    public static PublisherEntity publisherEntity(Long id) {
        var publisherEntity = new PublisherEntity();

        if (id != null) {
            ReflectionTestUtils.setField(publisherEntity, "id", id);
        }

        publisherEntity.setName("Spooky records");
        publisherEntity.setAddress("1 Spooky avenue");
        publisherEntity.setContactDetails("+1 217-703-2085");

        return publisherEntity;
    }
}

class ArtistTestData {
    private ArtistTestData() { }

    public static ArtistEntity artistEntity(Long id, String name, String biography) {
        var artistEntity = new ArtistEntity();

        if (id != null) {
            ReflectionTestUtils.setField(artistEntity, "id", id);
        }

        artistEntity.setName(name);
        artistEntity.setBiography(biography);

        return artistEntity;
    }
}