package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.artist.ArtistRequestDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.entities.ArtistEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArtistDtoMapper implements DtoMapper<ArtistResponseDto, ArtistRequestDto, ArtistEntity> {
    @Override
    public ArtistResponseDto toDto(ArtistEntity model) {
        return new ArtistResponseDto(
                model.getId(),
                model.getName(),
                model.getBiography()
        );
    }

    @Override
    public List<ArtistResponseDto> toDto(List<ArtistEntity> models) {
        var result = new ArrayList<ArtistResponseDto>();

        for (ArtistEntity model : models) {
            result.add(this.toDto(model));
        }

        return result;
    }

    @Override
    public ArtistEntity toEntity(ArtistRequestDto artistDto) {
        var entity = new ArtistEntity();
        entity.setName(artistDto.getName());
        entity.setBiography(artistDto.getBiography());

        return entity;
    }
}