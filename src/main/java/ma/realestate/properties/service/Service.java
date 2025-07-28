package ma.realestate.properties.service;

import ma.realestate.properties.common.PagesResponse;
import ma.realestate.properties.dto.response.ChangeStatusResponse;
import ma.realestate.properties.dto.util.SortInput;
import ma.realestate.properties.entity.enumu.StatusProperty;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Service <RES,REQ> {
     Optional<RES> add(REQ request);
     Optional<RES> remove(UUID id);
     Optional<RES> getById(UUID id);
     Optional<ChangeStatusResponse<RES>> update(UUID id, REQ request);
     List<RES> getAll(String search, SortInput sort);
     PagesResponse<RES> paginate(String search, SortInput sort,int page, int size);

     default Sort toSort(SortInput sortInput) {
          if (sortInput == null || sortInput.fields() == null || sortInput.fields().isEmpty()) {
               return Sort.unsorted();
          }
          return Sort.by(
                  sortInput.fields().stream()
                          .map(f -> new Sort.Order(f.direction(), f.field()))
                          .toList()
          );
     }
     default Optional<ChangeStatusResponse<RES>> changeResponse(RES response, boolean verified) {
          return Optional.ofNullable(ChangeStatusResponse.buildResponse(response,verified));
     }
}
