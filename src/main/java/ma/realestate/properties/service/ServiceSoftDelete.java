package ma.realestate.properties.service;

import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.util.SortInput;

import java.util.Optional;
import java.util.UUID;

public interface ServiceSoftDelete <RES>{
    PagesResponse<RES> trash(String search, SortInput sort, int page, int size);
    Optional<RES> recovory(UUID id);
    Optional<RES> destroy(UUID id);

}
