package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.entities.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

// The assignment suggested to have this extended mapper extend from the AlbumDtoMapper, but I prefer implementing a new mapper based on the DtoMapper interface.
// This is more explicit in my opinion, and it prevents inheritance. It follows the composition over inheritance principle.
// We can achieve the same result by injection the class we were going to extend from, and assign it in this class' fields.
@Component
public class AlbumExtendedDtoMapper implements DtoMapper<AlbumExtendedResponseDto, AlbumRequestDto, AlbumEntity> {
    private final AlbumDtoMapper albumDtoMapper;
    private final StockDtoMapper stockDtoMapper;
    private final ArtistDtoMapper artistDtoMapper;

    public AlbumExtendedDtoMapper(AlbumDtoMapper albumDtoMapper, StockDtoMapper stockDtoMapper, ArtistDtoMapper artistDtoMapper) {
        this.albumDtoMapper = albumDtoMapper;
        this.stockDtoMapper = stockDtoMapper;
        this.artistDtoMapper = artistDtoMapper;
    }

    @Override
    public AlbumExtendedResponseDto toDto(AlbumEntity model) {
        var albumResponseDto = this.albumDtoMapper.toDto(model);
        var stockItems = model.getStockItems();
        var artists = model.getArtists();

        return new AlbumExtendedResponseDto(
                albumResponseDto.getId(),
                albumResponseDto.getTitle(),
                albumResponseDto.getReleaseYear(),
                albumResponseDto.getGenre(),
                albumResponseDto.getPublisher(),
                stockItems != null
                        ? this.stockDtoMapper.toDto(new ArrayList<>(stockItems))
                        : new ArrayList<>(),
                artists != null
                        ? this.artistDtoMapper.toDto(new ArrayList<>(artists))
                        : new ArrayList<>()
        );
    }

    @Override
    public List<AlbumExtendedResponseDto> toDto(List<AlbumEntity> models) {
        var result = new ArrayList<AlbumExtendedResponseDto>();

        for (AlbumEntity model : models) {
            result.add(this.toDto(model));
        }

        return result;
    }

    @Override
    public AlbumEntity toEntity(AlbumRequestDto model) {
        return this.albumDtoMapper.toEntity(model);
    }
}