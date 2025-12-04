package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.PublisherEntity;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {
}
