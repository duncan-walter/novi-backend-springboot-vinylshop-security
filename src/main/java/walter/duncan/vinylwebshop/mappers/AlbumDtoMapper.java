package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.entities.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlbumDtoMapper implements DtoMapper<AlbumResponseDto, AlbumRequestDto, AlbumEntity> {
    private final GenreDtoMapper genreDtoMapper;
    private final PublisherDtoMapper publisherDtoMapper;

    public AlbumDtoMapper(GenreDtoMapper genreDtoMapper, PublisherDtoMapper publisherDtoMapper) {
        this.genreDtoMapper = genreDtoMapper;
        this.publisherDtoMapper = publisherDtoMapper;
    }

    @Override
    public AlbumResponseDto toDto(AlbumEntity model) {
        var genre = model.getGenre();
        var publisher = model.getPublisher();

        return new AlbumResponseDto(
                model.getId(),
                model.getTitle(),
                model.getReleaseYear(),
                genre != null ? genreDtoMapper.toDto(genre) : null,
                publisher != null ? publisherDtoMapper.toDto(publisher) : null
        );
    }

    @Override
    public List<AlbumResponseDto> toDto(List<AlbumEntity> models) {
        var result = new ArrayList<AlbumResponseDto>();

        for (AlbumEntity model : models) {
            result.add(this.toDto(model));
        }

        return result;
    }

    @Override
    public AlbumEntity toEntity(AlbumRequestDto albumDto) {
        var entity = new AlbumEntity();
        entity.setTitle(albumDto.getTitle());
        entity.setReleaseYear(albumDto.getReleaseYear());

        return entity;
    }
}