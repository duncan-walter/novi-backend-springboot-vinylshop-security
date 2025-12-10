package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.entities.AlbumEntity;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.repositories.AlbumRepository;

import java.util.List;

@Service
public class AlbumService extends BaseService<AlbumEntity, Long> {
    private final AlbumDtoMapper albumDtoMapper;

    protected AlbumService(AlbumRepository albumRepository, AlbumDtoMapper albumDtoMapper) {
        super(albumRepository);
        this.albumDtoMapper = albumDtoMapper;
    }

    public List<AlbumResponseDto> findAllAlbums() {
        return this.albumDtoMapper.toDto(this.repository.findAll());
    }

    public AlbumResponseDto findAlbumById(Long id) {
        return this.albumDtoMapper.toDto(this.getExistingById(id));
    }

    public AlbumResponseDto createAlbum(AlbumRequestDto albumRequestDto) {
        var albumEntity = this.albumDtoMapper.toEntity(albumRequestDto);

        return this.albumDtoMapper.toDto(this.repository.save(albumEntity));
    }

    @Transactional
    public AlbumResponseDto updateAlbum(Long id, AlbumRequestDto albumRequestDto) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setTitle("placeholder");
        persistedEntity.setReleaseYear(9999);

        return this.albumDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deleteAlbum(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}