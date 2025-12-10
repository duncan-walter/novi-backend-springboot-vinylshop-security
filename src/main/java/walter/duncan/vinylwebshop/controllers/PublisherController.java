package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.publisher.PublisherRequestDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
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
    public ResponseEntity<PublisherResponseDto> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(this.publisherService.findPublisherById(id));
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponseDto>> getPublishers() {
        return ResponseEntity.ok(this.publisherService.findAllPublishers());
    }

    @PostMapping
    public ResponseEntity<PublisherResponseDto> createPublisher(@Valid @RequestBody PublisherRequestDto publisherRequestDto) {
        var publisherResponseDto = this.publisherService.createPublisher(publisherRequestDto);
        var location = this.urlHelper.getResourceUri(publisherResponseDto.id());

        return ResponseEntity.created(location).body(publisherResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(@PathVariable Long id, @Valid @RequestBody PublisherRequestDto publisherRequestDto) {
        var publisherResponseDto = this.publisherService.updatePublisher(id, publisherRequestDto);
        var location = this.urlHelper.getResourceUri(publisherResponseDto.id());

        return ResponseEntity.ok().location(location).body(publisherResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePublisher(@PathVariable Long id) {
        this.publisherService.deletePublisher(id);

        return ResponseEntity.noContent().build();
    }
}