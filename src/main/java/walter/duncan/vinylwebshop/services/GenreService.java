package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;

import walter.duncan.vinylwebshop.dtos.genre.GenreRequestDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.entities.GenreEntity;
import walter.duncan.vinylwebshop.mappers.GenreDtoMapper;
import walter.duncan.vinylwebshop.repositories.GenreRepository;

import java.util.List;

@Service
public class GenreService extends BaseService<GenreEntity, Long> {
    private final GenreDtoMapper genreDtoMapper;

    public GenreService(GenreRepository genreRepository, GenreDtoMapper genreDtoMapper) {
        super(genreRepository);
        this.genreDtoMapper = genreDtoMapper;
    }

    public List<GenreResponseDto> findAllGenres() {
        return this.genreDtoMapper.toDto(this.repository.findAll());
    }

    public GenreResponseDto findGenreById(Long id) {
        return this.genreDtoMapper.toDto(this.getExistingById(id));
    }

    public GenreResponseDto createGenre(GenreRequestDto genreRequestDto) {
        var genreEntity = this.genreDtoMapper.toEntity(genreRequestDto);

        return this.genreDtoMapper.toDto(this.repository.save(genreEntity));
    }

    public GenreResponseDto updateGenre(Long id, GenreRequestDto genreRequestDto) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(genreRequestDto.getName());
        persistedEntity.setDescription(genreRequestDto.getDescription());

        return this.genreDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    public void deleteGenre(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}