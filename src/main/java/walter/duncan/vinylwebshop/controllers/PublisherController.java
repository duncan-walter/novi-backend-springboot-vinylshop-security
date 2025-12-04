package walter.duncan.vinylwebshop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.entities.PublisherEntity;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final UrlHelper urlHelper;
    private final PublisherService publisherService;

    public PublisherController(UrlHelper urlHelper, PublisherService publisherService) {
        this.urlHelper = urlHelper;
        this.publisherService = publisherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherEntity> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(this.publisherService.findPublisherById(id));
    }

    @GetMapping
    public ResponseEntity<List<PublisherEntity>> getPublishers() {
        return ResponseEntity.ok(this.publisherService.findAllPublishers());
    }

    @PostMapping
    public ResponseEntity<PublisherEntity> createPublisher(@RequestBody PublisherEntity publisher) {
        var publisherEntity = this.publisherService.createPublisher(publisher);
        var location = this.urlHelper.getResourceUri(publisherEntity.getId());

        return ResponseEntity.created(location).body(publisherEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherEntity> updatePublisher(@PathVariable Long id, @RequestBody PublisherEntity publisher) {
        var updatePublisher = this.publisherService.updatePublisher(id, publisher);
        var location = this.urlHelper.getResourceUri(updatePublisher.getId());

        return ResponseEntity.ok().location(location).body(updatePublisher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePublisher(@PathVariable Long id) {
        this.publisherService.deletePublisher(id);

        return ResponseEntity.noContent().build();
    }
}