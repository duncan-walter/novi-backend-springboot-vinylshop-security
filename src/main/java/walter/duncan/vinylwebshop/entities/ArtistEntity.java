package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
public class ArtistEntity extends BaseEntity {
    @Length(min = 2, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Length(max = 5000)
    @Column(name = "biography", length = 5000)
    private String biography;

    @ManyToMany(mappedBy = "artists")
    private Set<AlbumEntity> albums = new HashSet<>();;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}