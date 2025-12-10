package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.entities.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlbumDtoMapper implements DtoMapper<AlbumResponseDto, AlbumRequestDto, AlbumEntity> {
    @Override
    public AlbumResponseDto toDto(AlbumEntity model) {
        return null;
    }

    @Override
    public List<AlbumResponseDto> toDto(List<AlbumEntity> models) {
        return new ArrayList<>();
    }

    @Override
    public AlbumEntity toEntity(AlbumRequestDto albumDto) {
        return null;
    }
}