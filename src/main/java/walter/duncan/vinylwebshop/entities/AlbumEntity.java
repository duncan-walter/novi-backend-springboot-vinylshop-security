package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Table(name = "albums")
public class AlbumEntity extends BaseEntity {
    @Length(min = 3, max = 100)
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Min(1877)
    @Max(2100)
    @Column(name = "release_year")
    private int releaseYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisher;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockEntity> stockItems;

    @ManyToMany(mappedBy = "albums")
    private Set<ArtistEntity> artists;

    // It was tempting to choose @OneToOne here, but then a genre can only be referenced by one album.
    // In reality albums can share the same genre of course, hence why I picked @ManyToOne.
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

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