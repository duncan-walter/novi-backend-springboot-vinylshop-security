import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "edited_date")
    private LocalDateTime editDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        editDate = createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        editDate = LocalDateTime.now();
    }

    // Getters en Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
    }
}

