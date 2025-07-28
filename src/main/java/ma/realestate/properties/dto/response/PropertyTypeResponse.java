package ma.realestate.properties.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypeResponse {
    private UUID id;
    private String name;
    private String createdAt;
    private String deleteAt;
}
