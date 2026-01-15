package walter.duncan.vinylwebshop.dtos.album;

import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;

import java.util.List;

public class AlbumExtendedResponseDto extends AlbumResponseDto {
    private final List<StockResponseDto> stockItems;

    private final List<ArtistResponseDto> artists;

    public AlbumExtendedResponseDto(
            Long id,
            String title,
            int releaseYear,
            GenreResponseDto genre,
            PublisherResponseDto publisher,
            List<StockResponseDto> stockItems,
            List<ArtistResponseDto> artists
    ) {
        super(id, title, releaseYear, genre, publisher);
        this.stockItems = stockItems;
        this.artists = artists;
    }

    public List<StockResponseDto> getStockItems() {
        return stockItems;
    }

    public List<ArtistResponseDto> getArtists() {
        return artists;
    }
}