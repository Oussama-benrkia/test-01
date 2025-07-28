package ma.realestate.properties.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.realestate.properties.entity.enumu.StatusProperty;

import java.util.Set;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private double latitude;
    private double longitude;
    private String publishedAt;
    private String address;
    private String city;
    private StatusProperty status;
    private boolean isVerified;
    private boolean isPublished;
    private String createAt;
    private String deleteAt;
    private PropertyDetailsResponse extra;
    private PropertyTypeResponse type;
    private Set<PropertyFeaturesResponse> features;



}
