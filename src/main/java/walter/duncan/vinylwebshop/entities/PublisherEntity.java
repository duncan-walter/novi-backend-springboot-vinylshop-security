package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "publishers")
public class PublisherEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_details")
    private String contactDetails;

    @OneToMany(mappedBy = "publisher")
    private Set<AlbumEntity> albums;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactDetails() {
        return this.contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }
}