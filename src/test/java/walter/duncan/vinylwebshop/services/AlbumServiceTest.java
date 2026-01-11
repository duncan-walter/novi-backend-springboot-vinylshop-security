package walter.duncan.vinylwebshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.*;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.mappers.AlbumExtendedDtoMapper;
import walter.duncan.vinylwebshop.repositories.AlbumRepository;

import java.util.HashSet;
import java.util.List;

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

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllAlbums_shouldReturnListOfAlbumExtendedResponseDtos() {
        // Arrange
        var albumExtendedResponseDtos = List.of(
                AlbumTestData.albumExtendedDto(1L, 1L),
                AlbumTestData.albumExtendedDto(1L, 2L)
        );

        var genreEntity = new GenreEntity();
        ReflectionTestUtils.setField(genreEntity, "id", 1L);
        genreEntity.setName("Halloween");
        genreEntity.setDescription("Very spooky");

        var publisherEntity = new PublisherEntity();
        ReflectionTestUtils.setField(publisherEntity, "id", 1L);
        publisherEntity.setName("Spooky records");
        publisherEntity.setAddress("1 Spooky avenue");
        publisherEntity.setContactDetails("+1 217-703-2085");

        var stockEntity = new StockEntity();
        ReflectionTestUtils.setField(stockEntity, "id", 1L);
        stockEntity.setCondition("Used");
        stockEntity.setPrice(10.0);

        var artistEntity = new ArtistEntity();
        ReflectionTestUtils.setField(artistEntity, "id", 1L);
        artistEntity.setName("Mr. Skeleton");
        artistEntity.setBiography("He has lived his year spooking many people with his music, now he's dead. Serves him right.");

        var albumEntity1 = AlbumTestData.albumEntity(1L);
        albumEntity1.setGenre(genreEntity);
        albumEntity1.setPublisher(publisherEntity);
        albumEntity1.setStockItems(new HashSet<>(List.of(stockEntity)));
        albumEntity1.setArtists(new HashSet<>(List.of(artistEntity)));

        var albumEntity2 = AlbumTestData.albumEntity(2L);
        albumEntity1.setGenre(genreEntity);
        albumEntity1.setPublisher(publisherEntity);
        albumEntity1.setStockItems(new HashSet<>(List.of()));
        albumEntity1.setArtists(new HashSet<>(List.of(artistEntity)));

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
    void findAlbumById() {
    }

    @Test
    void createAlbum() {
        // Captor gebruiken om te oefenen
    }

    @Test
    void updateAlbum() {
    }

    @Test
    void deleteAlbum() {
    }

    @Test
    void linkArtist() {
    }

    @Test
    void unlinkArtist() {
    }
}

// TODO: Introduce builder
//  Used in tests as a proof of concept, this can be expanded to a proper builder pattern that builds complex album relations.
class AlbumTestData {
    private AlbumTestData() { }

    public static AlbumEntity albumEntity(Long id) {
        var albumEntity = new AlbumEntity();
        ReflectionTestUtils.setField(albumEntity, "id", id);
        albumEntity.setTitle("The Nightmare Before Christmas");
        albumEntity.setReleaseYear(1993);

        return albumEntity;
    }

    public static AlbumExtendedResponseDto albumExtendedDto(Long id, Long stockId) {
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