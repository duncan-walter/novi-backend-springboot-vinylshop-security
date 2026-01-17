package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.dtos.profile.ProfileAlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.profile.ProfileResponseDto;
import walter.duncan.vinylwebshop.services.ProfileService;

import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping()
    public ResponseEntity<List<ProfileResponseDto>> getProfiles(Authentication authentication) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if(authorities.contains("ROLE_ADMIN")){
            return ResponseEntity.ok(profileService.findAllProfiles());
        } else if (authorities.contains("ROLE_USER")){
            return ResponseEntity.ok(List.of(profileService.findOrCreateProfile(authentication)));
        } else {
            return ResponseEntity.status(403).build();
        }

    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumResponseDto>> getAlbum(Authentication authentication){
        String keyCloakId = authentication.getName();
        return ResponseEntity.ok(profileService.getAlbumsForProfile(keyCloakId));
    }

    @PostMapping("/albums")
    public ResponseEntity<Void> linkAlbum(Authentication authentication, @RequestBody @Valid ProfileAlbumRequestDto profileAlbumRequestDto){
        profileService.linkAlbum(authentication, profileAlbumRequestDto.getAlbumId());
        return ResponseEntity.ok().build();
    }
}