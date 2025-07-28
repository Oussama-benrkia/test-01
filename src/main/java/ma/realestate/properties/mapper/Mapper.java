package ma.realestate.properties.mapper;

import ma.realestate.properties.common.PagesResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public interface Mapper<REQ, RES, ENT> {
    RES toResponse(ENT entity);
    ENT toEntity(REQ request);

    default PagesResponse<RES> toPages(Page<ENT> page, Function<ENT, RES> converter) {
        return PagesResponse.<RES>builder()
                .content(page.getContent().stream()
                        .map(converter)
                        .toList())
                .page(page.getNumber())
                .limit(page.getSize())
                .last(page.isLast())
                .first(page.isFirst())
                .totalElements((int)page.getTotalElements())
                .size(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
