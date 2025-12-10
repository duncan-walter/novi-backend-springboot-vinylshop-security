package walter.duncan.vinylwebshop.mappers;

import org.springframework.stereotype.Component;

import walter.duncan.vinylwebshop.dtos.publisher.PublisherRequestDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.entities.PublisherEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublisherDtoMapper implements DtoMapper<PublisherResponseDto, PublisherRequestDto, PublisherEntity> {
    @Override
    public PublisherResponseDto toDto(PublisherEntity model) {
        return new PublisherResponseDto(
                model.getId(),
                model.getName(),
                model.getAddress(),
                model.getContactDetails()
        );
    }

    @Override
    public List<PublisherResponseDto> toDto(List<PublisherEntity> models) {
        var result = new ArrayList<PublisherResponseDto>();

        for (PublisherEntity model : models) {
            result.add(toDto(model));
        }

        return result;
    }

    @Override
    public PublisherEntity toEntity(PublisherRequestDto publisherDto) {
        var entity = new PublisherEntity();
        entity.setName(publisherDto.getName());
        entity.setAddress(publisherDto.getAddress());
        entity.setContactDetails(publisherDto.getContactDetails());

        return entity;
    }
}