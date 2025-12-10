package walter.duncan.vinylwebshop.dtos.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArtistRequestDto {
    @NotBlank(message = "Name must not be empty or whitespace.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    private String name;

    @Size(max = 5000, message = "Biography must not exceed 5000 characters.")
    private String biography;

    public String getName() {
        return this.name;
    }

    public String getBiography() {
        return this.biography;
    }
}