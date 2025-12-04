package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.GenreEntity;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}