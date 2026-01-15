package walter.duncan.vinylwebshop.dtos.publisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PublisherRequestDto {
    @NotBlank(message = "Name must not be empty or whitespace.")
    @Size(max = 50, message = "Name must be between 2 and 100 characters.")
    private String name;

    private String address;

    private String contactDetails;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactDetails() {
        return contactDetails;
    }
}