package walter.duncan.vinylwebshop.dtos.stock;

import jakarta.validation.constraints.*;

public class StockRequestDto {
    @Size(min = 2, max = 100, message = "Condition must be between 2 and 100 characters.")
    private String condition;

    @Positive
    @Max(value = (long)Double.MAX_VALUE, message = "Price exceeded maximum allowed value.")
    @NotNull(message = "Price is required.")
    private double price;

    public String getCondition() {
        return this.condition;
    }

    public double getPrice() {
        return this.price;
    }
}