package ma.realestate.properties.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PropertyDetailsResponse {
    private Integer bathRooms;
    private Integer bedRooms;
    private Boolean singleBedRooms;
    private Boolean doubleBedRooms;
    private Integer kitchens;
    private Integer rooms;
    private Integer maxPersonnes;
}
