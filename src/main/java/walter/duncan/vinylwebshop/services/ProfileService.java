package walter.duncan.vinylwebshop.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.dtos.profile.ProfileResponseDto;
import walter.duncan.vinylwebshop.entities.ProfileEntity;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.mappers.ProfileDtoMapper;
import walter.duncan.vinylwebshop.repositories.ProfileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService extends BaseService<ProfileEntity, Long, ProfileRepository> {
    private final ProfileDtoMapper profileDtoMapper;
    private final AlbumService albumService;
    private final AlbumDtoMapper albumMapper;

    public ProfileService(
            ProfileRepository profileRepository,
            ProfileDtoMapper profileDtoMapper,
            AlbumService albumService,
            AlbumDtoMapper albumMapper
    ) {
        super(profileRepository, ProfileEntity.class);
        this.profileDtoMapper = profileDtoMapper;
        this.albumService = albumService;
        this.albumMapper = albumMapper;
    }

    public List<ProfileResponseDto> findAllProfiles() {
        return this.profileDtoMapper.toDto(this.repository.findAll());
    }

    public ProfileResponseDto findOrCreateProfile(Authentication authentication) {
        return this.profileDtoMapper.toDto(findOrCreateProfileEntity(authentication.getName()));
    }

    private ProfileEntity findOrCreateProfileEntity(String keyCloakId){
        if (this.repository.existsByKeyCloakId(keyCloakId)) {
            return this.repository.findByKeyCloakId(keyCloakId).get();
        } else {
            return createProfileEntity(keyCloakId);
        }
    }

    private ProfileEntity createProfileEntity(String keyCloakId) {
        var profileEntity = new ProfileEntity();
        profileEntity.setKeyCloakId(keyCloakId);

        return this.repository.save(profileEntity);
    }

    public void linkAlbum(Authentication authentication, Long albumId) {
        var keyCloakId = authentication.getName();
        var profileEntity = findOrCreateProfileEntity(keyCloakId);
        var albumEntity = albumService.getExistingById(albumId);
        profileEntity.addAlbum(albumEntity);

        this.repository.save(profileEntity);
    }

    public List<AlbumResponseDto> getAlbumsForProfile(String keyCloakId) {
        var profileEntity = findOrCreateProfileEntity(keyCloakId);

        return albumMapper.toDto(new ArrayList<>(profileEntity.getAlbums()));
    }
}