package walter.duncan.vinylwebshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "stocks")
public class StockEntity extends BaseEntity {
    @Column(name = "condition", nullable = false)
    private String condition;

    @Max((long)Double.MAX_VALUE)
    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}