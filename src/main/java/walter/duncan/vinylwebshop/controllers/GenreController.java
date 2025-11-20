package walter.duncan.vinylwebshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.entities.Genre;
import walter.duncan.vinylwebshop.services.GenreService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Ik weet dat GetMapping dit automatisch doet, maar hoe explicieter hoe beter!
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(genreService.findGenreById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Genre>> getGenres() {
        try {
            return ResponseEntity.ok(this.genreService.findAllGenres());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        try {
            var createdGenre = this.genreService.createGenre(genre);
            var location = new URI(String.format("http://localhost:8080/genres/%s", createdGenre.getId()));

            return ResponseEntity.created(location).body(createdGenre);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        try {
            var updatedGenre = this.genreService.updateGenre(id, genre);
            var location = new URI(String.format("http://localhost:8080/genres/%s", id));

            return ResponseEntity.ok().location(location).body(updatedGenre);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteGenre(@PathVariable Long id) {
        try {
            this.genreService.deleteGenre(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // De aangeleverde service gooit deze exception nooit, maar voor de netheid zet ik dit er toch bij.
            return ResponseEntity.notFound().build();
        }
    }
}