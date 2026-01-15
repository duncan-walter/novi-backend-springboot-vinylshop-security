package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.AlbumEntity;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    List<AlbumEntity> findByStockItemsNotEmpty();

    List<AlbumEntity> findByStockItemsEmpty();

    List<AlbumEntity> findByGenreId(Long genreId);
}