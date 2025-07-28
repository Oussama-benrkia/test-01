package ma.realestate.properties.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
public class PropertyDetails {
    @Id
    private UUID id;
    private Integer bathRooms;
    private Integer bedRooms;
    private Boolean singleBedRooms;
    private Boolean doubleBedRooms;
    private Integer kitchens;
    private Integer rooms;
    private Integer maxPersonnes;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Property property;
}
