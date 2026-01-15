package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import walter.duncan.vinylwebshop.entities.ArtistEntity;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    List<ArtistEntity> findArtistsByAlbumsId(Long albumId);
}