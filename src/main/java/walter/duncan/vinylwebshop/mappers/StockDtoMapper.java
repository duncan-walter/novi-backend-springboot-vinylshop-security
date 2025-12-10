package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.stock.StockRequestDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.StockEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockDtoMapper implements DtoMapper<StockResponseDto, StockRequestDto, StockEntity> {
    @Override
    public StockResponseDto toDto(StockEntity model) {
        return new StockResponseDto(
                model.getId(),
                model.getCondition(),
                model.getPrice()
        );
    }

    @Override
    public List<StockResponseDto> toDto(List<StockEntity> models) {
        var result = new ArrayList<StockResponseDto>();

        for (StockEntity model : models) {
            result.add(this.toDto(model));
        }

        return result;
    }

    @Override
    public StockEntity toEntity(StockRequestDto stockDto) {
        var entity = new StockEntity();
        entity.setCondition(stockDto.getCondition());
        entity.setPrice(stockDto.getPrice());

        return entity;
    }
}