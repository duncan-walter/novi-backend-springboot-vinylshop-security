package walter.duncan.vinylwebshop.dtos.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreRequestDto {
    @NotBlank(message = "Name must not be empty or whitespace.")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters.")
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
