package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final UrlHelper urlHelper;
    private final AlbumService albumService;

    public AlbumController(UrlHelper urlHelper, AlbumService albumService) {
        this.urlHelper = urlHelper;
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumExtendedResponseDto> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(this.albumService.findAlbumById(id));
    }

    @GetMapping
    public ResponseEntity<List<AlbumExtendedResponseDto>> getAlbums() {
        return ResponseEntity.ok(this.albumService.findAllAlbums());
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDto> createAlbum(@RequestBody @Valid AlbumRequestDto albumRequestDto) {
        var albumResponseDto = this.albumService.createAlbum(albumRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .created(location)
                .body(albumResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDto> updateAlbum(@PathVariable Long id, @RequestBody @Valid AlbumRequestDto albumRequestDto) {
        var albumResponseDto = this.albumService.updateAlbum(id, albumRequestDto);
        var location = this.urlHelper.getResourceUri(0L);

        return ResponseEntity
                .ok()
                .location(location)
                .body(albumResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAlbum(@PathVariable Long id) {
        this.albumService.deleteAlbum(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/artists/{artistId}")
    public ResponseEntity<Void> linkArtist(@PathVariable Long id, @PathVariable Long artistId) {
        this.albumService.linkArtist(id, artistId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/artists/{artistId}")
    public ResponseEntity<Void> unlinkArtist(@PathVariable Long id, @PathVariable Long artistId) {
        this.albumService.unlinkArtist(id, artistId);

        return ResponseEntity.ok().build();
    }
}