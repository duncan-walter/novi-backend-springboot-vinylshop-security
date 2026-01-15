package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.stock.StockRequestDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.StockEntity;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolation;
import walter.duncan.vinylwebshop.exceptions.BusinessRuleViolationException;
import walter.duncan.vinylwebshop.mappers.StockDtoMapper;
import walter.duncan.vinylwebshop.repositories.StockRepository;

import java.util.List;

@Component
public class StockService extends BaseService<StockEntity, Long, StockRepository> {
    private final StockDtoMapper stockDtoMapper;
    private final AlbumService albumService;

    protected StockService(StockRepository stockRepository, StockDtoMapper stockDtoMapper, AlbumService albumService) {
        super(stockRepository, StockEntity.class);
        this.stockDtoMapper = stockDtoMapper;
        this.albumService = albumService;
    }

    public List<StockResponseDto> findAllStocksByAlbumId(Long albumId) {
        return this.stockDtoMapper.toDto(this.repository.findByAlbumId(albumId));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // Safe because isEmpty() is checked before using get()
    public StockResponseDto findStockByIdAndAlbumId(Long id, Long albumId) {
        var stocks = this.repository.findByIdAndAlbumId(id, albumId);

        if (stocks.isEmpty()) {
            this.throwResourceNotFoundException(id);
        }

        return this.stockDtoMapper.toDto(stocks.get());
    }

    public StockResponseDto createStock(StockRequestDto stockRequestDto, Long albumId) {
        var stockEntity = this.stockDtoMapper.toEntity(stockRequestDto);
        var persistedAlbumEntity = this.albumService.getExistingById(albumId);
        stockEntity.setAlbum(persistedAlbumEntity);

        return this.stockDtoMapper.toDto(this.repository.save(stockEntity));
    }

    @Transactional
    public StockResponseDto updateStock(StockRequestDto stockRequestDto, Long id, Long albumId) {
        var persistedEntity = getExistingById(id);
        var persistedAlbumEntity = this.albumService.getExistingById(albumId);

        if (!persistedEntity.getAlbum().getId().equals(persistedAlbumEntity.getId())) {
            throwResourceNotFoundException(id);
        }

        persistedEntity.setCondition(stockRequestDto.getCondition());
        persistedEntity.setPrice(stockRequestDto.getPrice());

        return this.stockDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deleteStock(Long id, Long albumId) {
        var persistedEntity = this.getExistingById(id);

        if (!persistedEntity.getAlbum().getId().equals(albumId)) {
            throw new BusinessRuleViolationException(
                    BusinessRuleViolation.STOCK_DOES_NOT_BELONG_TO_ALBUM,
                    String.format("Stock with id %s does not belong to album with id %s", id, albumId)
            );
        }

        this.repository.deleteByIdAndAlbumId(id, albumId);
    }
}