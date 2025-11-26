package walter.duncan.vinylwebshop.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import walter.duncan.vinylwebshop.entities.GenreEntity;
import walter.duncan.vinylwebshop.repositories.GenreRepository;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> findAllGenres() {
        return this.genreRepository.findAll();
    }

    public GenreEntity findGenreById(Long id) {
        return this.getExistingById(id);
    }

    public GenreEntity createGenre(GenreEntity genreEntity) {
        genreEntity.setId(null);

        return this.genreRepository.save(genreEntity);
    }

    public GenreEntity updateGenre(Long id, GenreEntity genreEntity) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(genreEntity.getName());
        persistedEntity.setDescription(genreEntity.getDescription());

        return this.genreRepository.save(persistedEntity);
    }

    public void deleteGenre(Long id) {
        this.ensureExistsById(id);
        this.genreRepository.deleteById(id);
    }

    private void ensureExistsById(Long id) {
        if (!this.genreRepository.existsById(id)) {
            this.throwNotFound(id);
        }
    }

    private GenreEntity getExistingById(Long id) {
        var genreEntity = this.genreRepository.findById(id);

        if (genreEntity.isEmpty()) {
            this.throwNotFound(id);
        }

        return genreEntity.get();
    }

    private void throwNotFound(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Genre with id " + id + " not found"
        );
    }
}