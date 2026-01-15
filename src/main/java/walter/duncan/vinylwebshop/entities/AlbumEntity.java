package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisher;

    @OneToMany(mappedBy = "album")
    private Set<StockEntity> stockItems = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "albums_artists",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<ArtistEntity> artists = new HashSet<>();;

    // It was tempting to choose @OneToOne here, but then a genre can only be referenced by one album.
    // In reality albums can share the same genre of course, hence why I picked @ManyToOne.
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
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

    public PublisherEntity getPublisher() {
        return this.publisher;
    }

    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }

    public Set<StockEntity> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockEntity> stockItems) {
        this.stockItems = stockItems;
    }

    public Set<ArtistEntity> getArtists() {
        return this.artists;
    }

    public void setArtists(Set<ArtistEntity> artists) {
        this.artists = artists;
    }

    public GenreEntity getGenre() {
        return this.genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    public void addArtist(ArtistEntity artistEntity) {
        this.artists.add(artistEntity);
    }

    public void removeArtist(ArtistEntity artistEntity) {
        this.artists.remove(artistEntity);
    }
}