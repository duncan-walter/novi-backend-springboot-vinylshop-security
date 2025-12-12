package walter.duncan.vinylwebshop.dtos.album;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlbumRequestDto {
    @NotBlank(message = "Title must not be empty or whitespace.")
    @Size(min = 3, max = 100, message = "Title must be between 2 and 100 characters.")
    private String title;

    @Min(value = 1877, message = "Release year must be at least 1877.")
    @Max(value = 2100, message = "Release year must not exceed 9999.")
    private int releaseYear;

    private Long genreId;

    private Long publisherId;

    public String getTitle() {
        return this.title;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public Long getGenreId() {
        return this.genreId;
    }

    public Long getPublisherId() {
        return this.publisherId;
    }
}