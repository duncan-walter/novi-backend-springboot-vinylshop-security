package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.stock.StockRequestDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.StockEntity;
import walter.duncan.vinylwebshop.mappers.StockDtoMapper;
import walter.duncan.vinylwebshop.repositories.StockRepository;

import java.util.List;

@Component
public class StockService extends BaseService<StockEntity, Long> {
    private final StockDtoMapper stockDtoMapper;

    protected StockService(StockRepository stockRepository, StockDtoMapper stockDtoMapper) {
        super(stockRepository);
        this.stockDtoMapper = stockDtoMapper;
    }

    public List<StockResponseDto> findAllStocks() {
        return this.stockDtoMapper.toDto(this.repository.findAll());
    }

    public StockResponseDto findStockById(Long id) {
        return this.stockDtoMapper.toDto(this.getExistingById(id));
    }

    public StockResponseDto createStock(StockRequestDto stockRequestDto) {
        var stockEntity = this.stockDtoMapper.toEntity(stockRequestDto);

        return this.stockDtoMapper.toDto(this.repository.save(stockEntity));
    }

    @Transactional
    public StockResponseDto updateStock(Long id, StockRequestDto stockRequestDto) {
        var persistedEntity = getExistingById(id);
        persistedEntity.setCondition("placeholder");
        persistedEntity.setPrice(9999);

        return this.stockDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deleteStock(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}