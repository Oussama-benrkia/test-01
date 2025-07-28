package ma.realestate.properties.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyFeaturesResponse {
    private UUID id;
    private String name;
    private String description;
    private Boolean autoRestore;
    private String createdAt;
}
