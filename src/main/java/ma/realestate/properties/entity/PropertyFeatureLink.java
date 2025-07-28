package ma.realestate.properties.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyFeatureLink {
    @EmbeddedId
    private PropertyFeatureLinkId id;
    @ManyToOne
    @MapsId("propertyid")
    private Property property;
    @ManyToOne
    @MapsId("featureid")
    private PropertyFeatures feature;
    private LocalDateTime deletedAt;
}
