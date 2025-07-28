package ma.realestate.properties.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyFeatureLinkId implements Serializable {
    private UUID propertyid;
    private UUID featureid;
}