package walter.duncan.vinylwebshop.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import walter.duncan.vinylwebshop.dtos.stock.StockRequestDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.helpers.UrlHelper;
import walter.duncan.vinylwebshop.services.StockService;

import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/stocks")
public class StockController {
    private final UrlHelper urlHelper;
    private final StockService stockService;

    public StockController(UrlHelper urlHelper, StockService stockService) {
        this.urlHelper = urlHelper;
        this.stockService = stockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDto> getStockByIdAndAlbumId(@PathVariable Long id, @PathVariable Long albumId) {
        return ResponseEntity.ok(this.stockService.findStockByIdAndAlbumId(id, albumId));
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDto>> getStocksByAlbumId(@PathVariable Long albumId) {
        return ResponseEntity.ok(this.stockService.findAllStocksByAlbumId(albumId));
    }

    @PostMapping
    public ResponseEntity<StockResponseDto> createStockByAlbumId(@PathVariable Long albumId, @RequestBody @Valid StockRequestDto stockRequestDto) {
        var stockResponseDto = this.stockService.createStock(stockRequestDto, albumId);
        var location = this.urlHelper.getResourceUri(stockResponseDto.id());

        return ResponseEntity
                .created(location)
                .body(stockResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDto> updateStockByAlbumId(@PathVariable Long id, @PathVariable Long albumId, @RequestBody @Valid StockRequestDto stockRequestDto) {
        var stockResponseDto = this.stockService.updateStock(stockRequestDto, id, albumId);
        var location = this.urlHelper.getResourceUri(stockResponseDto.id());

        return ResponseEntity
                .ok()
                .location(location)
                .body(stockResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStock(@PathVariable Long id, @PathVariable Long albumId) {
        this.stockService.deleteStock(id, albumId);

        return ResponseEntity.noContent().build();
    }
}