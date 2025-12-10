package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.genre.GenreRequestDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final UrlHelper urlHelper;
    private final GenreService genreService;

    public GenreController(UrlHelper urlHelper, GenreService genreService) {
        this.urlHelper = urlHelper;
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDto> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(this.genreService.findGenreById(id));
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getGenres() {
        return ResponseEntity.ok(this.genreService.findAllGenres());
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> createGenre(@Valid @RequestBody GenreRequestDto genreRequestDto) {
        var genreResponseDto = this.genreService.createGenre(genreRequestDto);
        var location = this.urlHelper.getResourceUri(genreResponseDto.id());

        return ResponseEntity.created(location).body(genreResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDto> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreRequestDto genreRequestDto) {
        var genreResponseDto = this.genreService.updateGenre(id, genreRequestDto);
        var location = this.urlHelper.getResourceUri(genreResponseDto.id());

        return ResponseEntity.ok().location(location).body(genreResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGenre(@PathVariable Long id) {
        this.genreService.deleteGenre(id);

        return ResponseEntity.noContent().build();
    }
}