package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;

import walter.duncan.vinylwebshop.entities.GenreEntity;
import walter.duncan.vinylwebshop.repositories.GenreRepository;

import java.util.List;

@Service
public class GenreService extends BaseService<GenreEntity, Long> {
    public GenreService(GenreRepository genreRepository) {
        super(genreRepository);
    }

    public List<GenreEntity> findAllGenres() {
        return this.repository.findAll();
    }

    public GenreEntity findGenreById(Long id) {
        return this.getExistingById(id);
    }

    public GenreEntity createGenre(GenreEntity genreEntity) {
        genreEntity.clearId();

        return this.repository.save(genreEntity);
    }

    public GenreEntity updateGenre(Long id, GenreEntity genreEntity) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(genreEntity.getName());
        persistedEntity.setDescription(genreEntity.getDescription());

        return this.repository.save(persistedEntity);
    }

    public void deleteGenre(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}