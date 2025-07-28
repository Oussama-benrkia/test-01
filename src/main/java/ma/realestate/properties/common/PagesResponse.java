package ma.realestate.properties.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.realestate.properties.dto.util.SortInput;
import org.springframework.data.domain.Page;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagesResponse<T> {
    private List<T> content;
    private int size;
    private int page;
    private int limit;
    private int totalPages;
    private int totalElements;
    private boolean first;
    private boolean last;
}
