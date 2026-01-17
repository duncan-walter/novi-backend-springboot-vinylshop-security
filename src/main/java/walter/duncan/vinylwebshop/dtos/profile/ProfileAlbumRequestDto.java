package walter.duncan.vinylwebshop.dtos.profile;

import jakarta.validation.constraints.NotNull;

public class ProfileAlbumRequestDto {
    @NotNull
    private Long albumId;

    public Long getAlbumId() {
        return this.albumId;
    }
}