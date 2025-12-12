package walter.duncan.vinylwebshop.services;

import org.springframework.data.jpa.repository.JpaRepository;

import walter.duncan.vinylwebshop.entities.BaseEntity;
import walter.duncan.vinylwebshop.exceptions.ResourceNotFoundException;

public abstract class BaseService<TEntity extends BaseEntity, TId, TRepository extends JpaRepository<TEntity, TId>> {
    protected final TRepository repository;
    private final Class<TEntity> entityClass;

    protected BaseService(TRepository repository, Class<TEntity> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    protected void ensureExistsById(TId id) {
        if (!this.repository.existsById(id)) {
            this.throwResourceNotFoundException(id);
        }
    }

    protected TEntity getExistingById(TId id) {
        var entity = this.repository.findById(id);

        if (entity.isEmpty()) {
            this.throwResourceNotFoundException(id);
        }

        return entity.get();
    }

    protected boolean isSameById(TEntity entity, TId id) {
        return entity != null && entity.getId().equals(id);
    }

    protected void throwResourceNotFoundException(TId id) {
        String entityName = entityClass.getSimpleName().replaceAll("Entity", "");
        throw new ResourceNotFoundException(entityName + " with id " + id + " not found.");
    }
}