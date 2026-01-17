package walter.duncan.vinylwebshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import walter.duncan.vinylwebshop.entities.ProfileEntity;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    boolean existsByKeyCloakId(String keyCloakId);

    Optional<ProfileEntity> findByKeyCloakId(String keyCloakId);
}