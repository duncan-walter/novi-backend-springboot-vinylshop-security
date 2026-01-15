package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.artist.ArtistRequestDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.entities.ArtistEntity;
import walter.duncan.vinylwebshop.mappers.ArtistDtoMapper;
import walter.duncan.vinylwebshop.repositories.ArtistRepository;

import java.util.List;

@Service
public class ArtistService extends BaseService<ArtistEntity, Long, ArtistRepository> {
    private final ArtistDtoMapper artistDtoMapper;

    protected ArtistService(ArtistRepository artistRepository, ArtistDtoMapper artistDtoMapper) {
        super(artistRepository, ArtistEntity.class);
        this.artistDtoMapper = artistDtoMapper;
    }

    public List<ArtistResponseDto> findAllArtists() {
        return this.artistDtoMapper.toDto(this.repository.findAll());
    }

    public ArtistResponseDto findArtistById(Long id) {
        return this.artistDtoMapper.toDto(this.getExistingById(id));
    }

    public ArtistResponseDto createArtist(ArtistRequestDto artistRequestDto) {
        var artistEntity = this.artistDtoMapper.toEntity(artistRequestDto);

        return this.artistDtoMapper.toDto(this.repository.save(artistEntity));
    }

    @Transactional
    public ArtistResponseDto updateArtist(Long id, ArtistRequestDto artistRequestDto) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(artistRequestDto.getName());
        persistedEntity.setBiography(artistRequestDto.getBiography());

        return this.artistDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deleteArtist(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}