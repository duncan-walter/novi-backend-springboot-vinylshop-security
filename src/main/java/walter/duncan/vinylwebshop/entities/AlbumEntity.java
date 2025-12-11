package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "albums")
public class AlbumEntity extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_year")
    @Max(9999)
    private int releaseYear;

    @ManyToOne()
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisher;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}