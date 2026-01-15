package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.StockEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByIdAndAlbumId(Long id, Long albumId);

    void deleteByIdAndAlbumId(Long id, Long albumId);

    List<StockEntity> findByAlbumId(Long albumId);
}