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
        return null;
    }

    @Override
    public List<ArtistResponseDto> toDto(List<ArtistEntity> models) {
        return new ArrayList<>();
    }

    @Override
    public ArtistEntity toEntity(ArtistRequestDto artistDto) {
        return null;
    }
}