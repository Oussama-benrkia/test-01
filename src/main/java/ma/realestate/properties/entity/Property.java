package ma.realestate.properties.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.realestate.properties.common.BaseEntity;
import ma.realestate.properties.entity.enumu.StatusProperty;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Property extends BaseEntity {

    private String name;
    @Column(unique = true)
    private String slug;
    private String description;
    private double latitude;
    private double longitude;
    private LocalDateTime publishedAt;
    private String address;
    private String city;
    @Enumerated(EnumType.STRING)
    private StatusProperty status = StatusProperty.DRAFT;
    private boolean isVerified = false;
    private boolean isPublished = false;

    @ManyToOne
    private PropertyType type;
    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private PropertyDetails details;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<PropertyFeatureLink> links;
}
