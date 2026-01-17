package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profiles")
public class ProfileEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String keyCloakId;

    @ManyToMany
    private Set<AlbumEntity> albums = new HashSet<>();

    public String getKeyCloakId() {
        return this.keyCloakId;
    }

    public void setKeyCloakId(String keyCloakId) {
        this.keyCloakId = keyCloakId;
    }

    public Set<AlbumEntity> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<AlbumEntity> albums) {
        this.albums = albums;
    }

    public void addAlbum(AlbumEntity album) {
        this.albums.add(album);
    }
}