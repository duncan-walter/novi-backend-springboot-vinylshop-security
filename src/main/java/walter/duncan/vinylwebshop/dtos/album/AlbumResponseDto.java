package walter.duncan.vinylwebshop.dtos.album;

import com.fasterxml.jackson.annotation.JsonInclude;

import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;

public class AlbumResponseDto {
    private final Long id;
    private final String title;
    private final int releaseYear;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final GenreResponseDto genre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final PublisherResponseDto publisher;

    public AlbumResponseDto(Long id, String title, int releaseYear, GenreResponseDto genre, PublisherResponseDto publisher) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.publisher = publisher;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public GenreResponseDto getGenre() {
        return this.genre;
    }

    public PublisherResponseDto getPublisher() {
        return this.publisher;
    }
}