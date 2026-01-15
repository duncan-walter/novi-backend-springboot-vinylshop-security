package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import walter.duncan.vinylwebshop.dtos.publisher.PublisherRequestDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.entities.PublisherEntity;
import walter.duncan.vinylwebshop.mappers.PublisherDtoMapper;
import walter.duncan.vinylwebshop.repositories.PublisherRepository;

import java.util.List;

@Service
public class PublisherService extends BaseService<PublisherEntity, Long, PublisherRepository> {
    private final PublisherDtoMapper publisherDtoMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherDtoMapper publisherDtoMapper) {
        super(publisherRepository, PublisherEntity.class);
        this.publisherDtoMapper = publisherDtoMapper;
    }

    public List<PublisherResponseDto> findAllPublishers() {
        return this.publisherDtoMapper.toDto(this.repository.findAll());
    }

    public PublisherResponseDto findPublisherById(Long id) {
        return this.publisherDtoMapper.toDto(this.getExistingById(id));
    }

    public PublisherResponseDto createPublisher(PublisherRequestDto publisherRequestDto) {
        var publisherEntity = this.publisherDtoMapper.toEntity(publisherRequestDto);

        return this.publisherDtoMapper.toDto(this.repository.save(publisherEntity));
    }

    @Transactional
    public PublisherResponseDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(publisherRequestDto.getName());
        persistedEntity.setAddress(publisherRequestDto.getAddress());
        persistedEntity.setContactDetails(publisherRequestDto.getContactDetails());

        return this.publisherDtoMapper.toDto(this.repository.save(persistedEntity));
    }

    @Transactional
    public void deletePublisher(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}