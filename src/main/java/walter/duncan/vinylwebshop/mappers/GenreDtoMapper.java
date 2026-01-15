package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.genre.GenreRequestDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.entities.GenreEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDtoMapper implements DtoMapper<GenreResponseDto, GenreRequestDto, GenreEntity> {
    @Override
    public GenreResponseDto toDto(GenreEntity model) {
        return new GenreResponseDto(
                model.getId(),
                model.getName(),
                model.getDescription()
        );
    }

    @Override
    public List<GenreResponseDto> toDto(List<GenreEntity> models) {
        var result = new ArrayList<GenreResponseDto>();

        for (GenreEntity model : models) {
            result.add(toDto(model));
        }

        return result;
    }

    @Override
    public GenreEntity toEntity(GenreRequestDto genreDto) {
        var entity = new GenreEntity();
        entity.setName(genreDto.getName());
        entity.setDescription(genreDto.getDescription());

        return entity;
    }
}