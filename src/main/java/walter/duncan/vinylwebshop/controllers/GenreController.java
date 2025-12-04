package walter.duncan.vinylwebshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.entities.GenreEntity;
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
    @ResponseStatus(HttpStatus.OK) // Ik weet dat GetMapping dit automatisch doet, maar hoe explicieter hoe beter!
    public ResponseEntity<GenreEntity> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(this.genreService.findGenreById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GenreEntity>> getGenres() {
        return ResponseEntity.ok(this.genreService.findAllGenres());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GenreEntity> createGenre(@RequestBody GenreEntity genre) {
        var genreEntity = this.genreService.createGenre(genre);
        var location = this.urlHelper.getResourceUri(genreEntity.getId());

        return ResponseEntity.created(location).body(genreEntity);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GenreEntity> updateGenre(@PathVariable Long id, @RequestBody GenreEntity genre) {
        var updatedGenre = this.genreService.updateGenre(id, genre);
        var location = this.urlHelper.getResourceUri(updatedGenre.getId());

        return ResponseEntity.ok().location(location).body(updatedGenre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteGenre(@PathVariable Long id) {
        this.genreService.deleteGenre(id);

        return ResponseEntity.noContent().build();
    }
}