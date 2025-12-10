package walter.duncan.vinylwebshop.mappers;

import walter.duncan.vinylwebshop.entities.BaseEntity;

import java.util.List;

public interface DtoMapper<TResponse, TRequest, TEntity extends BaseEntity> {
    TResponse toDto(TEntity model);
    List<TResponse> toDto(List<TEntity> models);
    TEntity toEntity(TRequest model);
}