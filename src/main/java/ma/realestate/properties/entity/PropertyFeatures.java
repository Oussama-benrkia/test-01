package ma.realestate.properties.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.realestate.properties.common.BaseEntity;

import java.util.Set;
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyFeatures extends BaseEntity {
    private String name;
    private String description;
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<PropertyFeatureLink> links;
    @Column(columnDefinition = "boolean default false")
    private boolean autoRestore;
}
