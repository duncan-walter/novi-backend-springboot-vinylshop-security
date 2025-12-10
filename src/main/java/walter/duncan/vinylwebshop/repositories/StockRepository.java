package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
}