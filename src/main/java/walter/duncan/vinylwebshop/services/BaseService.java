package walter.duncan.vinylwebshop.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class BaseService<TEntity, TId> {
    protected final JpaRepository<TEntity, TId> repository;

    protected BaseService(JpaRepository<TEntity, TId> repository) {
        this.repository = repository;
    }

    protected void ensureExistsById(TId id) {
        if (!this.repository.existsById(id)) {
            this.throwNotFound(id);
        }
    }

    protected TEntity getExistingById(TId id) {
        var entity = this.repository.findById(id);

        if (entity.isEmpty()) {
            this.throwNotFound(id);
        }

        return entity.get();
    }

    private void throwNotFound(TId id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Entity with id " + id + " not found"
        );
    }
}