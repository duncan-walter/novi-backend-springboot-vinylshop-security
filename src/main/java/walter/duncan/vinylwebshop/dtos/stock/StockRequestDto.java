package walter.duncan.vinylwebshop.dtos.stock;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class StockRequestDto {
    @NotBlank(message = "Condition must not be empty or whitespace.")
    @Size(min = 2, max = 100, message = "Condition must be between 2 and 100 characters.")
    private String condition;

    @Positive
    @Max(value = (long)Double.MAX_VALUE, message = "Price exceeded maximum allowed value.")
    private double price;

    public String getCondition() {
        return this.condition;
    }

    public double getPrice() {
        return this.price;
    }
}