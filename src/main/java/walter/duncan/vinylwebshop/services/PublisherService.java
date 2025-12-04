package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;

import walter.duncan.vinylwebshop.entities.PublisherEntity;
import walter.duncan.vinylwebshop.repositories.PublisherRepository;

import java.util.List;

@Service
public class PublisherService extends BaseService<PublisherEntity, Long> {
    public PublisherService(PublisherRepository publisherRepository) {
        super(publisherRepository);
    }

    public List<PublisherEntity> findAllPublishers() {
        return this.repository.findAll();
    }

    public PublisherEntity findPublisherById(Long id) {
        return this.getExistingById(id);
    }

    public PublisherEntity createPublisher(PublisherEntity publisherEntity) {
        publisherEntity.clearId();

        return this.repository.save(publisherEntity);
    }

    public PublisherEntity updatePublisher(Long id, PublisherEntity publisherEntity) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(publisherEntity.getName());
        persistedEntity.setAddress(publisherEntity.getAddress());
        persistedEntity.setContactDetails(publisherEntity.getContactDetails());

        return this.repository.save(persistedEntity);
    }

    public void deletePublisher(Long id) {
        this.ensureExistsById(id);
        this.repository.deleteById(id);
    }
}