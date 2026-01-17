package walter.duncan.vinylwebshop.dtos.profile;

import java.util.List;

public record ProfileResponseDto(Long id, String keyCloakId, List<Long> albums) { }