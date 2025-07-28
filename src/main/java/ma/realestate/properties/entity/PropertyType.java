package ma.realestate.properties.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class PropertyType extends BaseEntity {
    private String name;
    @OneToMany(mappedBy = "type" ,cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<Property> properties;
}
