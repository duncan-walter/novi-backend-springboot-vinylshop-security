package walter.duncan.vinylwebshop.dtos.album;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlbumRequestDto {
    @NotBlank(message = "Title must not be empty or whitespace.")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters.")
    private String title;

    @Max(value = 9999, message = "Release year must not exceed 9999.")
    private int releaseYear;

    public String getTitle() {
        return this.title;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }
}