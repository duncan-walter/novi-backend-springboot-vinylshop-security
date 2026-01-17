package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;
import walter.duncan.vinylwebshop.dtos.profile.ProfileResponseDto;
import walter.duncan.vinylwebshop.entities.BaseEntity;
import walter.duncan.vinylwebshop.entities.ProfileEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileDtoMapper {
    public ProfileResponseDto toDto(ProfileEntity entity){
        return new ProfileResponseDto(
                entity.getId(),
                entity.getKeyCloakId(),
                entity.getAlbums().stream().map(BaseEntity::getId).toList()
        );
    }

    public List<ProfileResponseDto> toDto(List<ProfileEntity> entities){
        var result = new ArrayList<ProfileResponseDto>();

        for (ProfileEntity entity : entities) {
            result.add(this.toDto(entity));
        }

        return result;
    }
}