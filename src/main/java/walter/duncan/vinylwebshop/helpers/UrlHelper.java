package walter.duncan.vinylwebshop.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class UrlHelper {
    private final HttpServletRequest request;

    public UrlHelper(HttpServletRequest request) {
        this.request = request;
    }

    public URI getResourceUri(Long id) {
        var requestPath = this.request.getRequestURL().toString();
        var createdResourcePath = requestPath + "/" + id;

        return URI.create(createdResourcePath);
    }
}