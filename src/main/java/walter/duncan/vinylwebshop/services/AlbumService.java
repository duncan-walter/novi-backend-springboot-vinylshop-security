package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.entities.AlbumEntity;
import walter.duncan.vinylwebshop.entities.GenreEntity;
import walter.duncan.vinylwebshop.entities.PublisherEntity;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolation;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolationException;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.mappers.AlbumExtendedDtoMapper;
import walter.duncan.vinylwebshop.repositories.AlbumRepository;

import java.util.List;

@Service
public class AlbumService extends BaseService<AlbumEntity, Long, AlbumRepository> {
    private final AlbumDtoMapper albumDtoMapper;
    private final AlbumExtendedDtoMapper albumExtendedDtoMapper;
    private final GenreService genreService;
    private final PublisherService publisherService;
    private final ArtistService artistService;

    protected AlbumService(
            AlbumRepository albumRepository,
            AlbumDtoMapper albumDtoMapper,
            AlbumExtendedDtoMapper albumExtendedDtoMapper,
            GenreService genreService,
            PublisherService publisherService,
            ArtistService artistService
    ) {
        super(albumRepository, AlbumEntity.class);
        this.albumDtoMapper = albumDtoMapper;
        this.albumExtendedDtoMapper = albumExtendedDtoMapper;
        this.genreService = genreService;
        this.publisherService = publisherService;
        this.artistService = artistService;
    }

    public List<AlbumExtendedResponseDto> findAllAlbums() {
        return this.albumExtendedDtoMapper.toDto(this.repository.findAll());
    }

    public AlbumExtendedResponseDto findAlbumById(Long id) {
        return this.albumExtendedDtoMapper.toDto(this.getExistingById(id));
    }

    @Transactional
    public AlbumResponseDto createAlbum(AlbumRequestDto albumRequestDto) {
        var albumEntity = this.albumDtoMapper.toEntity(albumRequestDto);

        if (albumRequestDto.getGenreId() != null) {
            /* TODO: INTRODUCE DOMAIN MODELS - ARCHITECTURAL / MAPPING.
                The usual way to find a resource in the current approach is by using the findXById() method, which returns a response dto.
                A genre response dto cannot be used to set the genre of an album through the setGenre() method of an album entity as it expects a genre entity.
                Right now the protected getExistingById() method if being used to get an genre entity instead.
                This is only possible because AlbumService and GenreService are in the same package.
                In the future, as the application expands, this may not be possible anymore.
                Domain models can solve this by acting as man in the middle and services will return them instead of response dtos.
                These domain models may be mapped back to entities and response dtos and vice versa.
            */
            var genreEntity = this.genreService.getExistingById(albumRequestDto.getGenreId());
            albumEntity.setGenre(genreEntity);
        }

        if (albumRequestDto.getPublisherId() != null) {
            var publisherEntity = this.publisherService.getExistingById(albumRequestDto.getPublisherId());
            albumEntity.setPublisher(publisherEntity);
        }

        return this.albumDtoMapper.toDto(this.repository.save(albumEntity));
    }

    @Transactional
    public AlbumResponseDto updateAlbum(Long id, AlbumRequestDto albumRequestDto) {
        var persistedEntity = this.getExistingById(id);

        var genreId = albumRequestDto.getGenreId();
        GenreEntity genreEntity = null;
        if (genreId != null) {
            var persistedGenreEntity = persistedEntity.getGenre();
            genreEntity = this.genreService.isSameById(persistedGenreEntity, genreId)
                    ? persistedGenreEntity
                    : this.genreService.getExistingById(genreId);
        }

        var publisherId = albumRequestDto.getPublisherId();
        PublisherEntity publisherEntity = null;
        if (publisherId != null) {
            var persistedPublisherEntity = persistedEntity.getPublisher();
            publisherEntity = this.publisherService.isSameById(persistedPublisherEntity, publisherId)
                    ? persistedPublisherEntity
                    : this.publisherService.getExistingById(publisherId);
        }

        persistedEntity.setTitle(albumRequestDto.getTitle());
        persistedEntity.setReleaseYear(albumRequestDto.getReleaseYear());
        persistedEntity.setGenre(genreEntity);
        persistedEntity.setPublisher(publisherEntity);

        return this.albumDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deleteAlbum(Long id) {
        var persistedEntity = this.getExistingById(id);

        if (!persistedEntity.getStockItems().isEmpty()) {
            throw new BusinessRuleViolationException(
                    BusinessRuleViolation.CANNOT_DELETE_ALBUM_WHEN_IT_HAS_STOCK,
                    String.format("Unable to delete album with id %s since it has stock linked to it", id)
            );
        }

        this.repository.deleteById(id);
    }

    @Transactional
    public void linkArtist(Long id, Long artistId) {
        // TODO: Check if artist it already present in album's artists.
        var persistedEntity = this.getExistingById(id);
        var persistedArtistEntity = this.artistService.getExistingById(artistId);
        persistedEntity.addArtist(persistedArtistEntity);

        this.repository.save(persistedEntity);
    }

    @Transactional
    public void unlinkArtist(Long id, Long artistId) {
        // TODO: Check if artist it already present in album's artists.
        var persistedEntity = this.getExistingById(id);
        var persistedArtistEntity = this.artistService.getExistingById(artistId);
        persistedEntity.removeArtist(persistedArtistEntity);

        this.repository.save(persistedEntity);
    }
}