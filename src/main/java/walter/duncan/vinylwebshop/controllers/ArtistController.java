package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.artist.ArtistRequestDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.ArtistService;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final UrlHelper urlHelper;
    private final ArtistService artistService;

    public ArtistController(UrlHelper urlHelper, ArtistService artistService) {
        this.urlHelper = urlHelper;
        this.artistService = artistService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDto> getArtistById(@PathVariable Long id) {
        return ResponseEntity.ok(this.artistService.findArtistById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponseDto>> getArtists() {
        return ResponseEntity.ok(this.artistService.findAllArtists());
    }

    @PostMapping
    public ResponseEntity<ArtistResponseDto> createAlbum(@RequestBody @Valid ArtistRequestDto artistRequestDto) {
        var artistResponseDto = this.artistService.createArtist(artistRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .created(location)
                .body(artistResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponseDto> updateArtist(@PathVariable Long id, @RequestBody @Valid ArtistRequestDto artistRequestDto) {
        var artistResponseDto = this.artistService.updateArtist(id, artistRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .ok()
                .location(location)
                .body(artistResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable Long id) {
        this.artistService.deleteArtist(id);

        return ResponseEntity.noContent().build();
    }
}